package shadowmage.ancient_warfare.common.aw_core.config;

/**
 * static/const/globals
 * @author Shadowmage
 *
 */
public class Config
{
/**
 * version numbering
 */
public static final String CORE_VERSION_MAJOR = "0.1.0";
public static final String CORE_VERSION_MINOR = "0";
public static final String CORE_VERSION_BUILD = "001";
public static final String CORE_BUILD_STATUS = "debug";

/**
 * mc versioning
 */
public static final String MC_VERSION = "1.4.5";

public static final String CORE_COMPLETE_VERSION = String.valueOf("MC"+MC_VERSION+"--"+CORE_VERSION_MAJOR+"."+CORE_VERSION_MINOR+"."+CORE_VERSION_BUILD+"-"+CORE_BUILD_STATUS);
public static final String VERSION_BOUNDS = String.valueOf("["+"MC"+MC_VERSION+"--"+CORE_VERSION_MAJOR+"."+CORE_VERSION_MINOR+"."+CORE_VERSION_BUILD+",)"); 



}
