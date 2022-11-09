package be.shop.slow_delivery.shop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BusinessTimeInfo {
    @Column(name = "opening_hours", nullable = true)
    private String openingHours;

    @Column(name = "days_off", nullable = true)
    private String dayOff;

    public BusinessTimeInfo(String openingHours, String dayOff) {
        this.openingHours = openingHours;
        this.dayOff = dayOff;
    }

    public void update(BusinessTimeInfo businessTimeInfo) {
        if(businessTimeInfo.getOpeningHours() != null)
            this.openingHours = businessTimeInfo.getOpeningHours();
        if(businessTimeInfo.getDayOff() != null)
            this.dayOff = businessTimeInfo.getDayOff();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessTimeInfo that = (BusinessTimeInfo) o;
        return Objects.equals(getOpeningHours(), that.getOpeningHours()) && Objects.equals(getDayOff(), that.getDayOff());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOpeningHours(), getDayOff());
    }
}
