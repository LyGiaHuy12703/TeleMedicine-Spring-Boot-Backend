package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.roomSchedule.RoomScheduleResponse;
import org.telemedicine.server.entity.RoomSchedule;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomScheduleMapper {
    RoomScheduleResponse toRoomScheduleResponse(RoomSchedule roomSchedule);
    List<RoomScheduleResponse> toRoomScheduleResponses(List<RoomSchedule> roomSchedules);
}
