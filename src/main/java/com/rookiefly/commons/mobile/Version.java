package com.rookiefly.commons.mobile;

/**
 * <p>版本号比较工具</p>
 *
 * <p>##完整模式 Version.of("0.1").eq("0.1.2"); // false</p>
 * 
 * <p>##不完整模式 Version.of("0.1").incomplete().eq("0.1.2"); // true</p>
 *
 * @author rookiefly
 */
public class Version {

	private static final String delimiter = "\\.";

	// 版本号
	private String version;

	// 是否完整模式，默认使用完整模式
	private boolean complete = true;

	/**
	 * 私有实例化构造方法
	 */
	private Version() {
	}

	private Version(String version) {
		this.version = version;
	}

	/**
	 * 不完整模式
	 * 
	 * @return {Version}
	 */
	public Version incomplete() {
		this.complete = false;
		return this;
	}

	/**
	 * 构造器
	 * 
	 * @param {Version}
	 */
	public static Version of(String version) {
		return new Version(version);
	}

	/**
	 * 比较版本号是否相同
	 * 
	 * example: * Version.of("0.3").eq("0.4")
	 * 
	 * 
	 * @param version
	 *            字符串版本号
	 * @return {boolean}
	 */
	public boolean eq(String version) {
		return compare(version) == 0;
	}

	public boolean gt(String version) {
		return compare(version) > 0;
	}

	public boolean gte(String version) {
		return compare(version) >= 0;
	}

	public boolean lt(String version) {
		return compare(version) < 0;
	}

	public boolean lte(String version) {
		return compare(version) <= 0;
	}

	/**
	 * 和另外一个版本号比较
	 * 
	 * @param version
	 * @return {int}
	 */
	private int compare(String version) {
		return Version.compare(this.version, version, complete);
	}

	public static int compare(String v1, String v2) {
		return compare(v1, v2, true);
	}

	/**
	 * 比较2个版本号
	 *
	 * @param v1
	 * @param v2
	 * @param complete
	 *            是否完整的比较两个版本
	 *
	 * @return (v1 < v2) ? -1 : ((v1 == v2) ? 0 : 1)
	 */
	public static int compare(String v1, String v2, boolean complete) {
		if (v1.equals(v2)) {
			return 0;
		}

		String[] version1Array = v1.split("\\.");
		String[] version2Array = v2.split("\\.");

		int index = 0;
		int minLen = Math.min(version1Array.length, version2Array.length);
		int diff = 0;

		while (index < minLen
		        && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
			index++;
		}

		if (diff == 0 && complete) {
			for (int i = index; i < version1Array.length; i++) {
				if (Integer.parseInt(version1Array[i]) > 0) {
					return 1;
				}
			}

			for (int i = index; i < version2Array.length; i++) {
				if (Integer.parseInt(version2Array[i]) > 0) {
					return -1;
				}
			}

			return 0;
		} else {
			return diff == 0 ? 0 :diff > 0 ? 1 : -1;
		}
	}

}
