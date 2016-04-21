package com.qio.model.asset;

import com.qio.model.assetType.AssetType;

public class AssetResponse extends Asset {
	private AssetType assetType;

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	// TODO:
	/*
	 * If two objects do not match, then its simply going to print out their
	 * string representations in the logger message. I need to figure out a
	 * better way for this.
	 */
	@Override
	public boolean equals(Object responseObj) {
		Boolean equalityCheckFlag = false;
		AssetType assetTypeFromResponseObj = ((AssetResponse) responseObj).getAssetType();

		if (super.equals(responseObj) && this.assetType.equals(assetTypeFromResponseObj))
			equalityCheckFlag = true;

		return equalityCheckFlag;
	}
}