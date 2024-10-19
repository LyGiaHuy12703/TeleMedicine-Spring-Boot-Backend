package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.service.ServiceRequest;
import org.telemedicine.server.dto.service.ServiceResponse;
import org.telemedicine.server.entity.ServiceEntity;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.ServiceMapper;
import org.telemedicine.server.repository.ServiceRepository;

import java.util.List;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceMapper serviceMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public ServiceResponse createService(ServiceRequest request) {
        if(serviceRepository.existsByType(request.getType())){
            throw new AppException(HttpStatus.BAD_REQUEST,"Service already exist", "service-e-01");
        };
        ServiceEntity service = serviceMapper.toService(request);
        serviceRepository.save(service);
        return serviceMapper.toServiceResponse(service);
    }
    public List<ServiceResponse> getAllsService() {
        List<ServiceEntity> service = serviceRepository.findAll();
        return serviceMapper.toServiceResponseList(service);
    }
    public ServiceResponse getServiceById(String specialtiesId) {
        ServiceEntity service = serviceRepository.findById(specialtiesId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "Service-e-02"));

        return serviceMapper.toServiceResponse(service);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ServiceResponse updateService(String id, ServiceRequest request) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "Service-e-02"));

        service.setPrice(request.getPrice());
        service.setType(request.getType());
        serviceRepository.save(service);
        return serviceMapper.toServiceResponse(service);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteService(String id) {
        serviceRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "Service-e-02"));
        serviceRepository.deleteById(id);
    }
}
