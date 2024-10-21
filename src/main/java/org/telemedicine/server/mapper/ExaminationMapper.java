package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.examination.ExaminationResponse;
import org.telemedicine.server.entity.Examination;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExaminationMapper {
    ExaminationResponse toExaminationResponse(Examination examination);
    List<ExaminationResponse> toExaminationResponses(List<Examination> examinations);
}
