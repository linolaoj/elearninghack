package com.liferay.service.override;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetLinkConstants;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetLinkLocalServiceUtil;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.service.DDLRecordSetLocalServiceWrapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * @author Marcellus
 */
@Component(
	immediate = true,
	property = {
	},
	service = ServiceWrapper.class
)
public class DDLRecordSetServiceOverride extends DDLRecordSetLocalServiceWrapper {

	public DDLRecordSetServiceOverride() {
		super(null);
	}

	@Override
	public DDLRecordSet addRecordSet(
			long userId, long groupId, long ddmStructureId, String recordSetKey,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			int minDisplayRows, int scope, ServiceContext serviceContext)
		throws PortalException {

		DDLRecordSet recordSet = super.addRecordSet(
			userId, groupId, ddmStructureId, recordSetKey, nameMap,
			descriptionMap, minDisplayRows, scope, serviceContext);

		// Asset

		updateAsset(
			userId, recordSet, serviceContext.getAssetCategoryIds(),
			serviceContext.getAssetTagNames(),
			serviceContext.getAssetLinkEntryIds(),
			serviceContext.getAssetPriority());

		return recordSet;
	}


	protected void updateAsset(
			long userId, DDLRecordSet recordSet, long[] assetCategoryIds,
			String[] assetTagNames, long[] assetLinkEntryIds, Double priority)
		throws PortalException {

		AssetEntry assetEntry = AssetEntryLocalServiceUtil.updateEntry(
			userId, recordSet.getGroupId(), DDLRecordSet.class.getName(),
			recordSet.getRecordSetId(), assetCategoryIds, assetTagNames);

		AssetLinkLocalServiceUtil.updateLinks(
			userId, assetEntry.getEntryId(), assetLinkEntryIds,
			AssetLinkConstants.TYPE_RELATED);
	}



}