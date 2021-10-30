package DataTransfer;

import java.time.LocalDate;

public class Request extends DataTransfer {
	private LocalDate startDate;
	private LocalDate EndDate;

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return this.EndDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.EndDate = endDate;
	}

}
