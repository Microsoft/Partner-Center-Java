// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license. See the LICENSE file in the project root for full license information.

package com.microsoft.store.partnercenter.subscriptions;

import java.text.MessageFormat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microsoft.store.partnercenter.BasePartnerComponent;
import com.microsoft.store.partnercenter.IPartner;
import com.microsoft.store.partnercenter.PartnerService;
import com.microsoft.store.partnercenter.models.subscriptions.SubscriptionRegistrationStatus;
import com.microsoft.store.partnercenter.models.utils.Tuple;
import com.microsoft.store.partnercenter.utils.StringHelper;

/**
 * This class implements the operations available on a customer's subscription registration status.
 */
public class SubscriptionRegistrationStatusOperations 
	extends BasePartnerComponent<Tuple<String, String>>
	implements ISubscriptionRegistrationStatus
{
	/**
	 * Initializes a new instance of the SubscriptionRegistrationStatusOperations class.
	 * 
	 * @param rootPartnerOperations The root partner operations instance.
	 * @param customerId            The customer identifier.
	 * @param subscriptionId        The subscription identifier
	 */
	public SubscriptionRegistrationStatusOperations( IPartner rootPartnerOperations, String customerId, String subscriptionId )
	{
		super( rootPartnerOperations, new Tuple<String, String>( customerId, subscriptionId ) );

		if ( StringHelper.isNullOrWhiteSpace( customerId ) )
		{
			throw new IllegalArgumentException( "customerId must be set." );
		}

		if ( StringHelper.isNullOrWhiteSpace( subscriptionId ) )
		{
			throw new IllegalArgumentException( "subscriptionId must be set." );
		}
	}

	/**
	 * Retrieves a subscription registration status.
	 * 
	 * @return The subscription registration status details.
	 */
	public SubscriptionRegistrationStatus get()
	{
		return this.getPartner().getServiceClient().get(
			this.getPartner(),
			new TypeReference<SubscriptionRegistrationStatus>(){}, 
			MessageFormat.format( 
				PartnerService.getInstance().getConfiguration().getApis().get("GetSubscriptionRegistrationStatus").getPath(),
				this.getContext().getItem1(),
				this.getContext().getItem2()));
	}
}