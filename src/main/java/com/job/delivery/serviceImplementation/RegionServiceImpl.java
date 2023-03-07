package com.job.delivery.serviceImplementation;

import com.job.delivery.entity.Place;
import com.job.delivery.entity.Region;
import com.job.delivery.exception.RegionException;
import com.job.delivery.repository.RegionRepository;
import com.job.delivery.repository.TransactionRepository;
import com.job.delivery.service.RegionService;

import java.util.*;
import java.util.stream.Collectors;

public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;
    private final TransactionRepository transactionRepository;

    public RegionServiceImpl(RegionRepository regionRepository, TransactionRepository transactionRepository) {
        this.regionRepository = regionRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<String> addRegion(Region region) {
        // Check if region already exists in database
        Optional<Region> optionalRegion = Optional.ofNullable(regionRepository.findByRegionName(region.getRegionName()));
        if (optionalRegion.isPresent()) {
            Region existingRegion = optionalRegion.get();
            Set<String> allPlaceNames = existingRegion.getPlaces().stream().map(Place::getPlaceName).collect(Collectors.toSet());
            allPlaceNames.addAll(region.getPlaces().stream().map(Place::getPlaceName).toList());
            existingRegion.setPlaces(new ArrayList<>(region.getPlaces()));
            regionRepository.save(existingRegion);
            return existingRegion.getPlaces().stream().map(Place::getPlaceName).toList();
        } else {
            // Check for duplicates in place names
            Set<String> uniquePlaceNames = region.getPlaces().stream().map(Place::getPlaceName).collect(Collectors.toSet());
            List<String> duplicatePlaceNames = new ArrayList<>();
            for (Place place : region.getPlaces()) {
                if (regionRepository.findByPlaceName(place.getPlaceName())) {
                    uniquePlaceNames.remove(place.getPlaceName());
                    duplicatePlaceNames.add(place.getPlaceName());
                }
            }
            // If there are duplicates, remove them and return bad request
            if (!duplicatePlaceNames.isEmpty()) {
                throw new RegionException("Duplicate place names:"  + duplicatePlaceNames);
            }
            // Otherwise, save region and return added place names
            region.setPlaces(new ArrayList<>(region.getPlaces()));
            regionRepository.save(region);
            return region.getPlaces().stream().map(Place::getPlaceName).toList();
        }
    }

    @Override
    public Map<String, List<Map<String, Object>>> getRegionsWithSameTransactionCount() {
        Map<String, List<Map<String, Object>>> responseMap = new HashMap<>();
        List<Map<String, Object>> responseList = new ArrayList<>();

        List<Object[]> resultList = transactionRepository.getRegionsWithSameTransactionCount();

        int transactionNumber = -1;
        List<Map<String, Object>> regions = new ArrayList<>();
        for (Object[] result : resultList) {
            int resultTransactionNumber = ((Number) result[0]).intValue();
            String regionName = (String) result[1];
            // Add other fields as needed

            if (resultTransactionNumber != transactionNumber) {
                // New transaction number encountered, create new entry in response
                if (!regions.isEmpty()) {
                    Map<String, Object> responseEntry = new HashMap<>();
                    responseEntry.put("transactionNumber", transactionNumber);
                    responseEntry.put("regions", regions);
                    responseList.add(responseEntry);
                }

                // Update current transaction number and clear regions list
                transactionNumber = resultTransactionNumber;
                regions = new ArrayList<>();
            }

            // Add region to current transaction number entry
            Map<String, Object> regionEntry = new HashMap<>();
            regionEntry.put("regionName", regionName);
            // Add other fields as needed
            regions.add(regionEntry);
        }

        // Add final transaction number entry to response
        if (!regions.isEmpty()) {
            Map<String, Object> responseEntry = new HashMap<>();
            responseEntry.put("transactionNumber", transactionNumber);
            responseEntry.put("regions", regions);
            responseList.add(responseEntry);
        }

        responseMap.put("response", responseList);
        return responseMap;
    }

}
