package com.qio.model.asset;

public class AssetRequest extends Asset {
	private String assetType;

	public AssetRequest() {
	}

	@SuppressWarnings("serial")
	public AssetRequest(String timeStamp, String assetType, String tenant) {
		super(timeStamp, tenant);
		this.assetType = assetType;
	}

	public AssetRequest(String abbreviation, String name, String description, String assetType, String tenant, String status) {
		super(abbreviation, name, description, tenant, status);
		this.assetType = assetType;
	}

	public AssetRequest(AssetRequest assetRequest) {
		this(assetRequest.getAbbreviation(), assetRequest.getName(), assetRequest.getDescription(), assetRequest.getAssetType(), assetRequest.getTenant(), assetRequest.getStatus());
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
}