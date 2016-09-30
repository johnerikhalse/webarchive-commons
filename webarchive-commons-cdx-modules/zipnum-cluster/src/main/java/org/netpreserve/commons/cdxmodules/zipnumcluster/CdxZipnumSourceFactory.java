/*
 * Copyright 2016 The International Internet Preservation Consortium.
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

package org.netpreserve.commons.cdxmodules.zipnumcluster;

import org.netpreserve.commons.cdx.CdxSource;
import org.netpreserve.commons.cdx.CdxSourceFactory;
import org.netpreserve.commons.uri.Uri;

/**
 *
 */
public class CdxZipnumSourceFactory extends CdxSourceFactory {
    @Override
    protected String getSupportedScheme() {
        return "zipnum";
    }

    @Override
    protected CdxSource createCdxSource(Uri identifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
