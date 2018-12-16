// -----------------------------------------------------------------------
// <copyright file="IPartnerServiceClient.java" company="Microsoft">
//      Copyright (c) Microsoft Corporation. All rights reserved.
// </copyright>
// -----------------------------------------------------------------------

package com.microsoft.store.partnercenter.network;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microsoft.store.partnercenter.IPartner;
import com.microsoft.store.partnercenter.models.Link;
import com.microsoft.store.partnercenter.models.utils.KeyValuePair;

public interface IPartnerServiceClient 
{
    /**
     * Executes a GET operation against the partner service. 
     * 
     * @param rootPartnerOperations An instance of the partner operations.
     * @param responseType The type of object to be returned.
     * @param link A link object that represents the action for accessing the resource. 
     */
    <T> T get(IPartner rootPartnerOperations, TypeReference<T> responseType, Link link);

    /**
     * Executes a GET operation against the partner service. 
     * 
     * @param rootPartnerOperations An instance of the partner operations.
     * @param responseType The type of object to be returned.
     * @param relativeUri The relative address of the request. 
     */
    <T> T get(IPartner rootPartnerOperations, TypeReference<T> responseType, String relativeUri);

    /**
     * Executes a GET operation against the partner service. 
     * 
     * @param rootPartnerOperations An instance of the partner operations.
     * @param responseType The type of object to be returned.
     * @param relativeUri The relative address of the request. 
     * @param headers Headers to be added to the request.
     * @param parameters Parameters to be added to the request.
     */
    <T> T get(IPartner rootPartnerOperations, TypeReference<T> responseType, String relativeUri, Map<String, String> headers, Collection<KeyValuePair<String, String>> parameters);

    /**
     * Executes a GET operation against the partner service. 
     * 
     * @param rootPartnerOperations An instance of the partner operations.
     * @param responseType The type of object to be returned.
     * @param relativeUri The relative address of the request. 
     * @param parameters Parameters to be added to the request.
     */
    <T> T get(IPartner rootPartnerOperations, TypeReference<T> responseType, String relativeUri, Collection<KeyValuePair<String, String>> parameters);
    
    /**
     * Executes a file content request against the partner service.
     * 
     * @param rootPartnerOperations An instance of the partner operations.
     * @param relativeUri The relative address of the request. 
     * @param acceptType The value for the accept type header.
     */
    InputStream getFileContents(IPartner rootPartnerOperations, String relativeUri, String acceptType);

    /**
     * Executes a HEAD operation against the partner service. 
     * 
     * @param rootPartnerOperations An instance of the partner operations.
     * @param responseType The type of object to be returned.
     * @param relativeUri The relative address of the request. 
     */
    <T> T head(IPartner rootPartnerOperations, TypeReference<T> responseType, String relativeUri);

    /**
     * Executes a PATCH operation against the partner service.
     * 
     * @param rootPartnerOperations An instance of the partner operations.
     * @param responseType The type of object to be returned.
     * @param relativeUri The relative address of the request. 
     * @param content The content for the body of the request.
     */
    <T, U> U patch(IPartner rootPartnerOperations, TypeReference<U> responseType, String relativeUri, T content);

    /**
     * Executes a POST operation against the partner service. 
     * 
     * @param rootPartnerOperations An instance of the partner operations. 
     * @param responseType The type of object to be returned.
     * @param relativeUri The relative address fo the request.
     * @param content The conent for the body of the request.
     */
    <T, U> U post(IPartner rootPartnerOperations, TypeReference<U> responseType, String relativeUri, T content);

    /**
     * Executes a POST operation against the partner service. 
     * 
     * @param rootPartnerOperations An instance of the partner operations. 
     * @param responseType The type of object to be returned.
     * @param relativeUri The relative address fo the request.
     * @param content The conent for the body of the request.
     * @param parameters Parameters to be added to the reqest.
     */
    <T, U> U post(IPartner rootPartnerOperations, TypeReference<U> responseType, String relativeUri, T content, Collection<KeyValuePair<String, String>> parameters);

    /**
     * Executes a PUT operation against the partner service. 
     * 
     * @param rootPartnerOperations An instance of the partner operations. 
     * @param responseType The type of object to be returned.
     * @param relativeUri The relative address fo the request.
     * @param content The conent for the body of the request.
     */
    <T, U> U put(IPartner rootPartnerOperations, TypeReference<U> responseType, String relativeUri, T content);

    /**
     * Executes a DELETE operation against the partner service. 
     * 
     * @param rootPartnerOperations An instance of the partner operations. 
     * @param responseType The type of object to be returned.
     * @param relativeUri The relative address fo the request.
     */
    <T> void delete(IPartner rootPartnerOperations, TypeReference<T> responseType, String relativeUri);
}