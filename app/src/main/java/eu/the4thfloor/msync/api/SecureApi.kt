/*
 * Copyright 2017 Ralph Bergmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.the4thfloor.msync.api

import eu.the4thfloor.msync.api.models.AccessResponse
import io.reactivex.Flowable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SecureApi {


    @POST("access")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    fun access(@FieldMap(encoded = true) params: Map<String, String>): Flowable<AccessResponse>
}
