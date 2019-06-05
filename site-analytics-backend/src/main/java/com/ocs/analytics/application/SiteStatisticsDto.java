package com.ocs.analytics.application;

import java.io.Serializable;

/**
 * The representation of the sitestatistics as it is used by the client application (aka. webpage) with the ability to
 * paginate the data. This object is stored in the session and the client can cycle the statistics data. The pages
 * are split up so that they contain data of a maximum amount of three days and the user can cycle through them per
 * day.
 *
 * @author Bas Piepers
 */

public class SiteStatisticsDto implements Serializable {
    private int pageCount;
    private int currentPage;
    private StatisticsPageDto page;

}
