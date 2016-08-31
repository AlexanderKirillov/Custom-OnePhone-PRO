package com.nowenui.customonephonepro;

class CheckLibrary {

	static String handleIllegalCharacterInResult(String result) {
		String tempResult = result;
		if (tempResult != null && tempResult.indexOf(" ") > 0) {
			tempResult = tempResult.replaceAll(" ", "_");
		}
		return tempResult;
	}

	static String checkValidData(String data) {
		String tempData = data;
		if (tempData == null || tempData.length() == 0) {
			tempData = "NA";
		}
		return tempData;
	}

	static String[] checkValidData(String[] data) {
		String[] tempData = data;
		if (tempData == null || tempData.length == 0) {
			tempData = new String[] { "-" };
		}
		return tempData;
	}
}
