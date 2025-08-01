package com.bottari.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.Setter;
import org.slf4j.Marker;

@Setter
public class MarkerFilter extends Filter<ILoggingEvent> {

    private String marker;

    @Override
    public FilterReply decide(final ILoggingEvent event) {
        final Marker marker = event.getMarker();
        if (marker != null && this.marker != null && marker.contains(this.marker)) {
            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }
}
