package org.example.Data;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.example.Data.RequestModel.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Core {
    private Requests requests = new Requests();
    private Query query = new Query();
    private static final int NUM_THREADS = 10;
    private ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

    private List<Query> getQuery(Location destination, Location origin, String date) {
        query.setDeparture(date);
        query.setOrigin(origin);
        query.setDestination(destination);
        return List.of(query);
    }

    private String prepareBody(City originCity, City destinationCity, String date, Integer peopleAmount) throws JsonProcessingException {
        String originC = originCity.getAirportCode();
        String destinC = destinationCity.getAirportCode();
        Location origin = new Location(originC);
        Location destination = new Location(destinC);
        Passengers passengers = new Passengers(peopleAmount, 0, 0, 0);
        FlightQuery flightQuery = new FlightQuery(getQuery(destination, origin, date), passengers, "");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(flightQuery);
    }

    private JSONArray checkIfResponseIsCorrect(String id) {
        JSONArray offersArray;
        do {
            Response getResponse = requests.sendGetRequest(id);
            JSONObject getJsonObject = new JSONObject(getResponse.prettyPrint());
            offersArray = getJsonObject.getJSONArray("offers");
        } while (offersArray.isEmpty());
        return offersArray;
    }

    private Float getLowestPriceFromResponseBody(JSONArray offersArray) {
        List<JSONObject> scoreObjectsList = new ArrayList<>();
        for (int i = 0; i < offersArray.length(); i++) {
            scoreObjectsList.add(offersArray.getJSONObject(i));
        }

        List<Float> listOfPriceAtDay = scoreObjectsList.stream()
                .map(score -> score.getJSONObject("score").getFloat("priceWithoutTransactionFee"))
                .collect(Collectors.toList());
        Optional<Float> lowestPrice = listOfPriceAtDay.stream().min(Comparator.naturalOrder());
        return lowestPrice.orElseGet(lowestPrice::get);
    }

    private String prepareDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    private float getCheapestPriceFromDay(City originCity, City destinationCity, LocalDate date, Integer peopleAmount) throws JsonProcessingException {
        String body = prepareBody(originCity, destinationCity, prepareDate(date), peopleAmount);
        Response postResponseFru = requests.sendPostRequest(body);
        JSONObject jsonResponse = new JSONObject(postResponseFru.prettyPrint());
        JSONArray arrayWithContent = checkIfResponseIsCorrect(jsonResponse.getString("id"));
        return getLowestPriceFromResponseBody(arrayWithContent);
    }

    private Map<LocalDate, Float> chepeastFromRangeDay(LocalDate startDate, LocalDate endDate, City originCity, City destinationCity, Integer peopleAmount) {
        Map<LocalDate, Float> cheapestPriceInDays = new ConcurrentHashMap<>();
        List<Future<?>> futures = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate) || currentDate.equals(endDate)) {
            LocalDate date = currentDate;
            Future<?> future = executor.submit(() -> {
                float price;
                try {
                    price = getCheapestPriceFromDay(originCity, destinationCity, date, peopleAmount);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                cheapestPriceInDays.put(date, price);
            });
            futures.add(future);
            currentDate = currentDate.plusDays(1);
        }

        // Wait for finish all the tasks
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return cheapestPriceInDays;
    }

    public Result cheapestFlyBothSides(LocalDate startDate, LocalDate endDate, City originCity, City destinationCity, Integer peopleAmount, Integer howLongStay) throws JsonProcessingException {
        Map<LocalDate, Float> originFlightData = chepeastFromRangeDay(startDate, endDate, originCity, destinationCity, peopleAmount);
        Map<LocalDate, Float> returnFlightData = chepeastFromRangeDay(startDate.plusDays(howLongStay), endDate.plusDays(howLongStay), destinationCity, originCity, peopleAmount);
        return findCheapestSum(originFlightData, returnFlightData, howLongStay);
    }

    public Result cheapestFlyBothSidesInRange(LocalDate startDate, LocalDate endDate, City originCity, City destinationCity, Integer peopleAmount, Integer minDaysStayIn, Integer maxDaysStayIn) throws JsonProcessingException {
        Map<LocalDate, Float> originFlightData = chepeastFromRangeDay(startDate, endDate, originCity, destinationCity, peopleAmount); //Map of flies to destination
        Map<LocalDate, Float> returnFlightData = chepeastFromRangeDay(startDate.plusDays(minDaysStayIn), endDate.plusDays(maxDaysStayIn), destinationCity, originCity, peopleAmount);
        return findCheapestSum(originFlightData, returnFlightData, minDaysStayIn, maxDaysStayIn);
    }

    private Result findCheapestSum(Map<LocalDate, Float> originFlightData, Map<LocalDate, Float> returnFlightData, Integer howLongStay) {
        List<Result> allPairsResults = originFlightData.keySet().stream()
                .map(dateFromMap -> {
                    Result result = new Result();
                    Float amountToDestination = originFlightData.get(dateFromMap);
                    Float amountToOrigin = returnFlightData.get(dateFromMap.plusDays(howLongStay));
                    result.setTotalAmount(amountToDestination + amountToOrigin);
                    result.setAmountToDestination(amountToDestination);
                    result.setAmountToOrigin(amountToOrigin);
                    result.setStartDate(dateFromMap);
                    result.setEndDate(dateFromMap.plusDays(howLongStay));
                    return result;
                }).sorted((oneResult, otherResult) -> Float.compare(oneResult.getTotalAmount(), otherResult.getTotalAmount())).collect(Collectors.toList());

        return allPairsResults.get(0);
    }

    private Result findCheapestSum(Map<LocalDate, Float> originFlightData, Map<LocalDate, Float> returnFlightData, Integer minDaysStayIn, Integer maxDaysStayIn) {
        final int daysStayInRange = maxDaysStayIn - minDaysStayIn;
        List<Result> allPairsResults = originFlightData.keySet().stream()
                .flatMap(startDate -> IntStream.range(0, daysStayInRange)
                        .mapToObj(j -> {
                            Result result = new Result();
                            Float amountToDestination = originFlightData.get(startDate);
                            Float amountToOrigin = returnFlightData.get(startDate.plusDays(minDaysStayIn + j));
                            result.setTotalAmount(amountToDestination + amountToOrigin);
                            result.setAmountToDestination(amountToDestination);
                            result.setAmountToOrigin(amountToOrigin);
                            result.setStartDate(startDate);
                            result.setEndDate(startDate.plusDays(minDaysStayIn + j));
                            return result;
                        }))
                .sorted((oneResult, otherResult) -> Float.compare(oneResult.getTotalAmount(), otherResult.getTotalAmount())).collect(Collectors.toList());

        return allPairsResults.get(0);
    }
}
