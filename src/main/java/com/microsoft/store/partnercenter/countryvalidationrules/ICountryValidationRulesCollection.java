// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license. See the LICENSE file in the project root for full license information.

package com.microsoft.store.partnercenter.countryvalidationrules;

import com.microsoft.store.partnercenter.IPartnerComponentString;
import com.microsoft.store.partnercenter.genericoperations.ICountrySelector;

/**
 * Interface and Template for Country-Specific Rules for Validation
 *
 */
public interface ICountryValidationRulesCollection 
	extends IPartnerComponentString, ICountrySelector<ICountryValidationRules>
{
    /**
     * Obtains behavior for a specific country's validation rules.
     *
     * @param country The country's ISO2 code.
     * @return The country validation rules operations.
     */
    ICountryValidationRules byCountry(String country);
}