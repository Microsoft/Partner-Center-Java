// -----------------------------------------------------------------------
// <copyright file="UserMemberCollectionOperations.java" company="Microsoft">
//      Copyright (c) Microsoft Corporation. All rights reserved.
// </copyright>
// -----------------------------------------------------------------------

package com.microsoft.store.partnercenter.customerdirectoryroles;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microsoft.store.partnercenter.BasePartnerComponent;
import com.microsoft.store.partnercenter.IPartner;
import com.microsoft.store.partnercenter.PartnerService;
import com.microsoft.store.partnercenter.models.SeekBasedResourceCollection;
import com.microsoft.store.partnercenter.models.query.IQuery;
import com.microsoft.store.partnercenter.models.query.QueryType;
import com.microsoft.store.partnercenter.models.roles.UserMember;
import com.microsoft.store.partnercenter.models.utils.KeyValuePair;
import com.microsoft.store.partnercenter.models.utils.Tuple;
import com.microsoft.store.partnercenter.utils.StringHelper;

/**
 * UserMemberCollectionOperations
 *
 * Used for managing membership and access to directory roles.
 */
public class UserMemberCollectionOperations 
	extends BasePartnerComponent<Tuple<String, String>> 
	implements IUserMemberCollection
{
	/**
	 * Initializes a new instance of the UserMemberCollectionOperations class
	 * 
	 * @param rootPartnerOperations The partner operations instance.
	 * @param customerId The customer identifier.
	 * @param roleId The directory role identifier.
	 */
	public UserMemberCollectionOperations( IPartner rootPartnerOperations, String customerId, String roleId )
	{
		super( rootPartnerOperations, new Tuple<String,String>( customerId, roleId ) );
		if ( StringHelper.isNullOrEmpty( customerId ) )
		{
			throw new IllegalArgumentException("customerId must be set.");
		}

		if ( StringHelper.isNullOrEmpty( roleId ) )
		{
			throw new IllegalArgumentException("roleId must be set.");
		}
	}

	/**
	 * Get a single user member operations object.
	 * 
	 * @param userId The user identifier.
	 * @return The user member operations instance.
	 */
	@Override
	public IUserMember byId(String userId)
	{
		return new UserMemberOperations( this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2(), userId );
	}

	/**
	 * Adds customer user to a directory role.
	 * 
	 * @param newEntity The user to be added,
	 * @return The customer directory role user member.
	 */
	@Override
	public UserMember create( UserMember newEntity )
	{
		if ( newEntity == null )
		{
			throw new IllegalArgumentException("The newEntity parameter cannot be null.");
		}
		
		return this.getPartner().getServiceClient().post(
			this.getPartner(), 
			new TypeReference<UserMember>(){},
			MessageFormat.format(
				PartnerService.getInstance().getConfiguration().getApis().get("AddUserToCustomerDirectoryRole").getPath(),
				this.getContext().getItem1(),
				this.getContext().getItem2()),
			newEntity);
	}

	/**
	 * Gets all the user members of a customer directory role.
	 * 
	 * @return The directory role user memberships.
	 */
	@Override
	public SeekBasedResourceCollection<UserMember> get()
	{
		return this.getPartner().getServiceClient().get(
			this.getPartner(),
			new TypeReference<SeekBasedResourceCollection<UserMember>>(){}, 
			MessageFormat.format( 
				PartnerService.getInstance().getConfiguration().getApis().get("GetCustomerDirectoryRoleUserMembers").getPath(),
				this.getContext().getItem1(),
				this.getContext().getItem2()));
	}

	/**
	 * Retrieves the user members of a customer directory role.
	 * 
	 * @param query A query to apply onto user member collection.
	 * @return The  directory role user memberships.
	 */
	@Override
	public SeekBasedResourceCollection<UserMember> query(IQuery query)
	{
		if ( query == null )
		{
			throw new IllegalArgumentException( "query can't be null" );
		}

		if ( query.getType() == QueryType.COUNT )
		{
			throw new IllegalArgumentException( "query can't be a count query." );
		}

		Collection<KeyValuePair<String, String>> parameters = new ArrayList<KeyValuePair<String, String>>();
		Map<String, String> headers = new HashMap<>();

		if ( query.getType() == QueryType.SEEK )
		{
			// if this is a seek query, add the seek operation and the continuation token to the request.
			if ( query.getToken() == null )
			{
				throw new IllegalArgumentException( "query.Token is required." );
			}

			headers.put(
				PartnerService.getInstance().getConfiguration().getApis().get("GetCustomerUsers").getAdditionalHeaders().get("ContinuationToken"),
				query.getToken().toString()
			); 
			
			parameters.add(
				new KeyValuePair<String, String>(
					PartnerService.getInstance().getConfiguration().getApis().get("GetCustomerDirectoryRoleUserMembers").getParameters().get("SeekOperation"),
					query.getSeekOperation().toString()));    
		}
		else
		{
			if ( query.getType() == QueryType.INDEXED )
			{
				parameters.add
				(
					new KeyValuePair<String, String>
					(
						PartnerService.getInstance().getConfiguration().getApis().get("GetCustomerDirectoryRoleUserMembers").getParameters().get("Size"),
						String.valueOf(query.getPageSize())
					) 
				);
			}
			else
			{
				parameters.add
				(
					new KeyValuePair<String, String>
					(
						PartnerService.getInstance().getConfiguration().getApis().get("GetCustomerDirectoryRoleUserMembers").getParameters().get("Size"),
						"0"
					) 
				);
			}
		}

		return this.getPartner().getServiceClient().get(
			this.getPartner(),
			new TypeReference<SeekBasedResourceCollection<UserMember>>(){}, 
			MessageFormat.format( 
				PartnerService.getInstance().getConfiguration().getApis().get("GetCustomerDirectoryRoleUserMembers").getPath(),
				this.getContext().getItem1(), 
				this.getContext().getItem2()),
			headers,
			parameters);
	}
}