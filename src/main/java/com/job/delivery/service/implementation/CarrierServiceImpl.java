package com.job.delivery.service.implementation;

import com.job.delivery.entity.Carrier;
import com.job.delivery.entity.Region;
import com.job.delivery.exception.CarrierException;
import com.job.delivery.repository.CarrierRepository;
import com.job.delivery.repository.RegionRepository;
import com.job.delivery.repository.TransactionRepository;
import com.job.delivery.service.CarrierService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

public class CarrierServiceImpl implements CarrierService {
    private final CarrierRepository carrierRepository;
    private final RegionRepository regionRepository;
    private final TransactionRepository transactionRepository;

    public CarrierServiceImpl(CarrierRepository carrierRepository, RegionRepository regionRepository, TransactionRepository transactionRepository) {
        this.carrierRepository = carrierRepository;
        this.regionRepository = regionRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<String> addCarrier(Carrier carrier) {
        Optional<Carrier> optionalCarrier = carrierRepository.findByCarrierName(carrier.getCarrierName());
        if (optionalCarrier.isPresent()) {
            Carrier existingCarrier = optionalCarrier.get();
            List<String> existingRegionNames = getRegionNames(existingCarrier.getRegions());
            Set<String> allRegionNames = new HashSet<>(existingRegionNames);
            allRegionNames.addAll(getRegionNames(carrier.getRegions()));
            existingCarrier.setRegions((ArrayList<Region>) getRegionsByName(new ArrayList<>(allRegionNames)));
            carrierRepository.save(existingCarrier);
            return getRegionNames(existingCarrier.getRegions());
        } else {
            Set<String> uniqueRegionNames = (Set<String>) getRegionNames(carrier.getRegions());
            List<String> duplicateRegionNames = new ArrayList<>();
            for (String regionName : uniqueRegionNames) {
                if (regionRepository.findByRegionName(regionName) != null) {
                    uniqueRegionNames.remove(regionName);
                    duplicateRegionNames.add(regionName);
                }
            }
            if (!duplicateRegionNames.isEmpty()) {
                throw new CarrierException("Duplicate region names: " + duplicateRegionNames);
            }
            carrier.setRegions((ArrayList<Region>) getRegionsByName(new ArrayList<>(uniqueRegionNames)));
            carrierRepository.save(carrier);
            return getRegionNames(carrier.getRegions());
        }
    }

    @Override
    public List<Optional<Carrier>> getCarriersForRegion(String regionName) {
        Region region = regionRepository.findByRegionName(regionName);
        if (region == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Region not found");
        }
        List<Optional<Carrier>> carriers = new ArrayList<>();
        for (String carrierName : carrierRepository.findDistinctCarrierNamesByRegionName(regionName)) {
            Optional<Carrier> carrier = carrierRepository.findByCarrierName(carrierName);
            if (carrier.isPresent()) {
                carriers.add(carrier);
            }
        }
        return carriers;
    }

    @Override
    public List<Map<String, Object>> getScorePerCarrier(List<String> carrierNames, int minimumScore) {
        List<Carrier> carriers = carrierRepository.findAllByNameIn(carrierNames);
        List<Object[]> scorePerCarrier = transactionRepository.getScorePerCarrier(carriers, minimumScore);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : scorePerCarrier) {
            Map<String, Object> map = new HashMap<>();
            Carrier carrier = (Carrier) row[0];
            Long totalScore = (Long) row[1];
            map.put("carrierName", carrier.getCarrierName());
            map.put("totalScore", totalScore);
            result.add(map);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("response", result);
        return Collections.singletonList(response);
    }


    private List<String> getRegionNames(List<Region> regions) {
        List<String> regionNames = new ArrayList<>();
        for (Region region : regions) {
            regionNames.add(region.getRegionName());
        }
        return regionNames;
    }

    private List<Region> getRegionsByName(List<String> regionNames) {
        List<Region> regions = new ArrayList<>();
        for (String regionName : regionNames) {
            Optional<Region> optionalRegion = Optional.ofNullable(regionRepository.findByRegionName(regionName));
            optionalRegion.ifPresent(regions::add);
        }
        return regions;
    }
}
