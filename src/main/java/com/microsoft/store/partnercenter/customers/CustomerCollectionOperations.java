// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license. See the LICENSE file in the project root for full license information.

package com.microsoft.store.partnercenter.customers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.store.partnercenter.BasePartnerComponentString;
import com.microsoft.store.partnercenter.IPartner;
import com.microsoft.store.partnercenter.PartnerService;
import com.microsoft.store.partnercenter.exception.PartnerErrorCategory;
import com.microsoft.store.partnercenter.exception.PartnerException;
import com.microsoft.store.partnercenter.models.SeekBasedResourceCollection;
import com.microsoft.store.partnercenter.models.customers.Customer;
import com.microsoft.store.partnercenter.models.query.IQuery;
import com.microsoft.store.partnercenter.models.query.QueryType;
import com.microsoft.store.partnercenter.models.utils.KeyValuePair;
import com.microsoft.store.partnercenter.relationshiprequests.CustomerRelationshipRequestOperations;
import com.microsoft.store.partnercenter.relationshiprequests.ICustomerRelationshipRequest;
import com.microsoft.store.partnercenter.usage.CustomerUsageRecordCollectionOperations;
import com.microsoft.store.partnercenter.usage.ICustomerUsageRecordCollection;
import com.microsoft.store.partnercenter.utils.ParameterValidator;

/**
 * The partner customers implementation.
 */
public class CustomerCollectionOperations
	extends BasePartnerComponentString
	implements ICustomerCollection
{
	/**
	 * The minimum allowed page size for the collection.
	 */
	private static final int MIN_PAGE_SIZE = 1;

	/**
	 * The maximum allowed page size for the collection.
	 */
	private static final int MAX_PAGE_SIZE = 500;

	/**
	 * The current partner's customer usage records operations.
	 */
	private ICustomerUsageRecordCollection usageRecords;

	/**
	 * The customer relationship request operations.
	 */
	private ICustomerRelationshipRequest relationshipRequest;

	/**
	 * Initializes a new instance of the CustomerCollectionOperations class.
	 * 
	 * @param rootPartnerOperations The root partner operations instance.
	 */
	public CustomerCollectionOperations( IPartner rootPartnerOperations )
	{
		super( rootPartnerOperations );
	}

	/**
	 * Obtains the customer usage record operations.
	 * 
	 * @return The customer usage record operations.
	 */
	@Override
	public ICustomerUsageRecordCollection getUsageRecords()
	{
		if ( this.usageRecords == null )
		{
			this.usageRecords = new CustomerUsageRecordCollectionOperations( this.getPartner() );
		}
		return this.usageRecords;
	}

	/**
	 * Obtains the customer relationship request operations.
	 * 
	 * @return The customer relationship request operations.
	 */
	@Override
	public ICustomerRelationshipRequest getRelationshipRequests()
	{
		if ( this.relationshipRequest == null )
		{
			this.relationshipRequest = new CustomerRelationshipRequestOperations( this.getPartner() );
		}
		return this.relationshipRequest;
	}

	/**
	 * Gets a single customer operations.
	 * 
	 * @param customerId The customer identifier.
	 * @return The customer operations.
	 */
	@Override
	public ICustomer byId( String customerId )
	{
		return new CustomerOperations( this.getPartner(), customerId );
	}

	/**
	 * Creates a new customer.
	 * 
	 * @param newCustomer The new customer information.
	 * @return The customer information that was just created.
	 */
	@Override
	public Customer create( Customer newCustomer )
	{
		if ( newCustomer == null )
		{
			throw new IllegalArgumentException("The newCustomer parameter cannot be null." );
		}

		return this.getPartner().getServiceClient().post(
			this.getPartner(), 
			new TypeReference<Customer>(){},
			MessageFormat.format(
				PartnerService.getInstance().getConfiguration().getApis().get("CreateCustomer").getPath(),
				this.getContext()),
			newCustomer);
	}

	/**
	 * Retrieves all customers associated to the partner.
	 *
	 * @return All customers.
	 */
	@Override
	public SeekBasedResourceCollection<Customer> get()
	{
		return this.getPartner().getServiceClient().get(
			this.getPartner(), 
			new TypeReference<SeekBasedResourceCollection<Customer>>(){},
			PartnerService.getInstance().getConfiguration().getApis().get( "GetCustomers" ).getPath());
	}

	/**
	 * Queries customers associated to the partner. - Count queries are not supported by this operation. - You can set
	 * the page size or filter or do both at the same time. - Sort is not supported. - You can navigate to other pages
	 * by specifying a seek query with the seek operation and the continuation token sent by the previous operation.
	 *
	 * @param customersQuery A query to apply onto customers. Check {@link com.microsoft.store.partnercenter.models.query.QueryFactory} to see how to build queries.
	 * @return The requested customers.
	 */
	@Override
	public SeekBasedResourceCollection<Customer> query( IQuery customersQuery )
	{
		if ( customersQuery == null )
		{
			throw new IllegalArgumentException( "customersQuery can't be null" );
		}

		if ( customersQuery.getType() == QueryType.COUNT )
		{
			throw new IllegalArgumentException( "customersQuery can't be a count query." );
		}

		Collection<KeyValuePair<String, String>> parameters = new ArrayList<KeyValuePair<String, String>>();
		Map<String, String> headers = new HashMap<>();

		if ( customersQuery.getType() == QueryType.SEEK )
		{
			// if this is a seek query, add the seek operation and the continuation token to the request
			if ( customersQuery.getToken() == null )
			{
				throw new IllegalArgumentException( "customersQuery.Token is required." );
			}

			headers.put(
				PartnerService.getInstance().getConfiguration().getApis().get("GetCustomers").getAdditionalHeaders().get("ContinuationToken"),
				customersQuery.getToken().toString());

			parameters.add( 
				new KeyValuePair<String, String>( 
					PartnerService.getInstance().getConfiguration().getApis().get("GetCustomers").getParameters().get("SeekOperation"),
					customersQuery.getSeekOperation().toString()));
		}
		else
		{
			if ( customersQuery.getType() == QueryType.INDEXED )
			{
				// if the query specifies a page size, validate it and add it to the request
				ParameterValidator.isIntInclusive(
					MIN_PAGE_SIZE, MAX_PAGE_SIZE, 
					customersQuery.getPageSize(),
					MessageFormat.format(
						"Allowed page size values are from {0}-{1}",
						MIN_PAGE_SIZE, 
						MAX_PAGE_SIZE));

				parameters.add( 
					new KeyValuePair<String, String>( 
						PartnerService.getInstance().getConfiguration().getApis().get("GetCustomers").getParameters().get("Size"),
						String.valueOf(customersQuery.getPageSize())));
			}
			else
			{
				parameters.add(
					new KeyValuePair<String, String>(
						PartnerService.getInstance().getConfiguration().getApis().get("GetCustomers").getParameters().get("Size"),
						"0"));
			}
			if ( customersQuery.getFilter() != null )
			{
				// add the filter to the request if specified
				ObjectMapper mapper = new ObjectMapper();
				
				try
				{
					parameters.add( 
						new KeyValuePair<String, String>( 
							PartnerService.getInstance().getConfiguration().getApis().get("GetCustomers").getParameters().get("Filter"),
							URLEncoder.encode(mapper.writeValueAsString(customersQuery.getFilter()),
							"UTF-8")));
				}
				catch ( UnsupportedEncodingException e )
				{
					throw new PartnerException( "", null, PartnerErrorCategory.REQUEST_PARSING, e );
				}
				catch ( JsonProcessingException e )
				{
					throw new PartnerException( "", null, PartnerErrorCategory.REQUEST_PARSING, e );
				}
			}
		}
		
		return this.getPartner().getServiceClient().get(
			this.getPartner(),
			new TypeReference<SeekBasedResourceCollection<Customer>>(){}, 
			PartnerService.getInstance().getConfiguration().getApis().get("GetCustomers").getPath(),
			headers,
			parameters);
	}
}