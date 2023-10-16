package br.com.americo.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

	@Test
	public void validEndDateGreaterThanStartDate() {

		LocalDateTime endDate = LocalDateTime.parse("2023-10-16T10:53:00");
		LocalDateTime startDate = LocalDateTime.parse("2023-10-15T11:30:00");
		Assertions.assertTrue(endDate.isAfter(startDate));
	}

	@Test
	public void validValidatesEqualDates() {

		LocalDate endDate = LocalDate.parse("2023-10-16");
		LocalDate startDate = LocalDate.parse("2023-10-16");
		Assertions.assertEquals(endDate, startDate);
	}

	@Test
	public void ValidatesIftheEndDateIsLessThanTheStartDate() {

		LocalDate endDate = LocalDate.parse("2023-10-16");
		LocalDate startDate = LocalDate.parse("2023-10-16");
		Assertions.assertTrue(endDate.isBefore(startDate));
	}
}
