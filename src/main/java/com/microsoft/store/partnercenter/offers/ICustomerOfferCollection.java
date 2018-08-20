// -----------------------------------------------------------------------
// <copyright file="ICustomerOfferCollection.java" company="Microsoft">
//      Copyright (c) Microsoft Corporation.  All rights reserved.
// </copyright>
// -----------------------------------------------------------------------

package com.microsoft.store.partnercenter.offers;

import com.microsoft.store.partnercenter.IPartnerComponentString;
import com.microsoft.store.partnercenter.genericoperations.IEntireEntityCollectionRetrievalOperations;
import com.microsoft.store.partnercenter.models.ResourceCollection;
import com.microsoft.store.partnercenter.models.offers.Offer;

public interface ICustomerOfferCollection
	extends IPartnerComponentString, IEntireEntityCollectionRetrievalOperations<Offer, ResourceCollection<Offer>>
{
    /***
     * Retrieves all the offers for the provided Customer.
     * 
     * @return: The offers for the provided customer.
     */
    ResourceCollection<Offer> get();
    
    /***
     *  Retrieves a subset of offers for the provided customer.
     *  
	 *  @param name="offset": The starting index.
     *  @param name="size": The maximum number of offers to return.
     *  @return: The requested segment of the offers for the provided customer.
     */
    ResourceCollection<Offer> get( int offset, int size );
}