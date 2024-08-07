package com.srpinfotec.cvslog.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {
    public static LocalDateTime changeTimeZoneUtcToAsia(LocalDateTime from){
        // 시간대 변환 (UTC -> Asia / Seoul)
        ZonedDateTime utcTime = from.atZone(ZoneId.of("UTC"));
        ZonedDateTime localTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return localTime.toLocalDateTime();
    }
}
