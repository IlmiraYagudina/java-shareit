package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.util.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingShortDto {
    @NotNull(message = "Время создания заявки на бронирование не должно быть пустым", groups = {Create.class})
    @FutureOrPresent(message = "Время начала заявки не должно быть в прошлом", groups = {Create.class})
    private LocalDateTime start;
    @NotNull(message = "Время окончания заявки на бронирование не должно быть пустым", groups = {Create.class})
    @Future(message = "Время окончания заявки не может быть в прошлом", groups = {Create.class})
    private LocalDateTime end;
    @NotNull(message = "Уникальный идентификатор не может отсутствовать.", groups = {Create.class})
    @Positive(message = "Уникальный идентификатор не может быть отрицательным.", groups = {Create.class})
    private Long itemId;
    private int bookerId;

}