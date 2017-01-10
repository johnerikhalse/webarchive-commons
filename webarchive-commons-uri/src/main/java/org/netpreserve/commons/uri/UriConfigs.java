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
package org.netpreserve.commons.uri;

import org.netpreserve.commons.uri.parser.Parser;
import org.netpreserve.commons.uri.parser.WhatwgConformantReferenceResolver;
import org.netpreserve.commons.uri.parser.LegacyWaybackSurtEncoder;
import org.netpreserve.commons.uri.parser.WhatwgConformantParser;
import org.netpreserve.commons.uri.parser.Rfc3986Parser;
import org.netpreserve.commons.uri.parser.Rfc3986ReferenceResolver;
import org.netpreserve.commons.uri.parser.LaxRfc3986Parser;
import org.netpreserve.commons.uri.normalization.AllLowerCase;
import org.netpreserve.commons.uri.normalization.CheckLongEnough;
import org.netpreserve.commons.uri.normalization.ConvertSlashes;
import org.netpreserve.commons.uri.normalization.InferCommonSchemesForSchemelessUri;
import org.netpreserve.commons.uri.normalization.LaxTrimming;
import org.netpreserve.commons.uri.normalization.OptimisticDnsScheme;
import org.netpreserve.commons.uri.normalization.StripErrorneousExtraSlashes;
import org.netpreserve.commons.uri.normalization.StripSessionId;
import org.netpreserve.commons.uri.normalization.StripTrailingEscapedSpaceOnAuthority;
import org.netpreserve.commons.uri.normalization.StripSlashAtEndOfPath;
import org.netpreserve.commons.uri.normalization.StripWwwN;
import org.netpreserve.commons.uri.normalization.WhatwgPreTrimming;
import org.netpreserve.commons.uri.normalization.RemoveLocalhost;
import org.netpreserve.commons.uri.normalization.TrimHost;
import org.netpreserve.commons.uri.normalization.WhatwgNormalizeHostAndPath;

/**
 * Common configurations to use with UriBuilder.
 */
public final class UriConfigs {

    /**
     * The default RFC3986 conformant parser.
     */
    public static final Parser DEFAULT_PARSER = new Rfc3986Parser();

    public static final Parser LAX_PARSER = new LaxRfc3986Parser();

    public static final Parser WHATWG_PARSER = new WhatwgConformantParser();

    public static final ReferenceResolver DEFAULT_REFERENCE_RESOLVER = new Rfc3986ReferenceResolver();

    public static final ReferenceResolver WHATWG_REFERENCE_RESOLVER = new WhatwgConformantReferenceResolver();

    public static final UriFormat DEFAULT_FORMAT = new UriFormat();

    public static final UriFormat HERITRIX_URI_FORMAT = new UriFormat()
            .ignoreFragment(true);

    public static final UriFormat CANONICALIZED_URI_FORMAT = new UriFormat()
            .ignoreUser(true)
            .ignorePassword(true)
            .ignoreFragment(true);

    public static final UriFormat SURT_KEY_FORMAT = new UriFormat()
            .surtEncoding(true)
            .ignoreScheme(true)
            .ignoreUser(true)
            .ignorePassword(true)
            .ignoreFragment(true)
            .decodeHost(true);

    public static final UriFormat LEGACY_SURT_KEY_FORMAT = new UriFormat()
            .surtEncoding(true)
            .ignoreScheme(true)
            .ignoreUser(true)
            .ignorePassword(true)
            .ignoreFragment(true)
            .decodeHost(false)
            .surtEncoder(new LegacyWaybackSurtEncoder());

    /**
     * A Uri config which only allows URI's which conforms to the RFC 3986.
     * <p>
     * It does some normalizing, but only normalizing which doesn't alter semantics. Case is normalized in places where
     * the URI is case insensitive. Path is normalized for constructs like '{@code foo/../bar/.}'.
     */
    public static final UriBuilderConfig STRICT = new UriBuilderConfig()
            .parser(UriConfigs.DEFAULT_PARSER)
            .referenceResolver(UriConfigs.DEFAULT_REFERENCE_RESOLVER)
            .requireAbsoluteUri(false)
            .strictReferenceResolution(true)
            .caseNormalization(true)
            .percentEncodingNormalization(true)
            .percentEncodingCase(UriBuilderConfig.HexCase.UPPER)
            .ipv6Case(UriBuilderConfig.HexCase.UPPER)
            .pathSegmentNormalization(true)
            .schemeBasedNormalization(true)
            .encodeIllegalCharacters(false)
            .defaultFormat(UriConfigs.DEFAULT_FORMAT);

    /**
     * A Uri config trying to mimic the behavior of major browsers as described by
     * <a herf="https://url.spec.whatwg.org/">whatwg.org</a>.
     */
    public static final UriBuilderConfig WHATWG = new UriBuilderConfig()
            .parser(UriConfigs.WHATWG_PARSER)
            .referenceResolver(UriConfigs.WHATWG_REFERENCE_RESOLVER)
            .requireAbsoluteUri(false)
            .strictReferenceResolution(false)
            .caseNormalization(true)
            .percentEncodingNormalization(false)
            .percentEncodingCase(UriBuilderConfig.HexCase.UPPER)
            .ipv6Case(UriBuilderConfig.HexCase.LOWER)
            .pathSegmentNormalization(true)
            .schemeBasedNormalization(true)
            .encodeIllegalCharacters(true)
            .punycodeUnknownScheme(true)
            .defaultFormat(UriConfigs.DEFAULT_FORMAT)
            .addNormalizer(new WhatwgPreTrimming())
            .addNormalizer(new WhatwgNormalizeHostAndPath())
            .addNormalizer(new RemoveLocalhost());

    /**
     * A forgiving URI config.
     * <p>
     * Normalizes some illegal characters, but otherwise tries to accept the URI as it is as much as possible.
     */
    public static final UriBuilderConfig LAX_URI = new UriBuilderConfig()
            .parser(UriConfigs.LAX_PARSER)
            .referenceResolver(UriConfigs.DEFAULT_REFERENCE_RESOLVER)
            .requireAbsoluteUri(false)
            .strictReferenceResolution(false)
            .caseNormalization(true)
            .percentEncodingNormalization(true)
            .percentEncodingCase(UriBuilderConfig.HexCase.UPPER)
            .ipv6Case(UriBuilderConfig.HexCase.UPPER)
            .pathSegmentNormalization(true)
            .schemeBasedNormalization(true)
            .encodeIllegalCharacters(true)
            .defaultFormat(UriConfigs.DEFAULT_FORMAT)
            .addNormalizer(new LaxTrimming())
            .addNormalizer(new ConvertSlashes())
            .addNormalizer(new StripErrorneousExtraSlashes())
            .addNormalizer(new StripSlashAtEndOfPath())
            .addNormalizer(new StripTrailingEscapedSpaceOnAuthority())
            .addNormalizer(new OptimisticDnsScheme())
            .addNormalizer(new CheckLongEnough());

    /**
     * A config that behave as the UsableUri used in Heritrix.
     */
    public static final UriBuilderConfig HERITRIX = new UriBuilderConfig()
            .parser(UriConfigs.LAX_PARSER)
            .referenceResolver(UriConfigs.DEFAULT_REFERENCE_RESOLVER)
            .requireAbsoluteUri(true)
            .strictReferenceResolution(false)
            .caseNormalization(true)
            .ipv6Case(UriBuilderConfig.HexCase.UPPER)
            .percentEncodingCase(UriBuilderConfig.HexCase.UPPER)
            .percentEncodingNormalization(true)
            .pathSegmentNormalization(true)
            .schemeBasedNormalization(true)
            .encodeIllegalCharacters(true)
            // Consider URIs too long for IE as illegal.
            .maxUriLength(2083)
            .defaultFormat(UriConfigs.HERITRIX_URI_FORMAT)
            .addNormalizer(new LaxTrimming())
            .addNormalizer(new ConvertSlashes())
            .addNormalizer(new StripErrorneousExtraSlashes())
            .addNormalizer(new StripSlashAtEndOfPath())
            .addNormalizer(new StripTrailingEscapedSpaceOnAuthority())
            .addNormalizer(new OptimisticDnsScheme())
            .addNormalizer(new CheckLongEnough())
            .addNormalizer(new TrimHost());

    /**
     * A config with the standard normalizations used in OpenWayback.
     */
    public static final UriBuilderConfig WAYBACK = new UriBuilderConfig()
            .parser(UriConfigs.LAX_PARSER)
            .referenceResolver(UriConfigs.WHATWG_REFERENCE_RESOLVER)
            .requireAbsoluteUri(true)
            .strictReferenceResolution(false)
            .caseNormalization(true)
            .percentEncodingNormalization(true)
            .pathSegmentNormalization(true)
            .schemeBasedNormalization(true)
            .encodeIllegalCharacters(true)
            // Consider URIs too long for IE as illegal.
            .maxUriLength(2083)
            .defaultFormat(UriConfigs.CANONICALIZED_URI_FORMAT)
            .addNormalizer(new LaxTrimming())
            .addNormalizer(new ConvertSlashes())
            .addNormalizer(new StripWwwN())
            .addNormalizer(new StripSessionId())
            .addNormalizer(new StripErrorneousExtraSlashes())
            .addNormalizer(new StripSlashAtEndOfPath())
            .addNormalizer(new StripTrailingEscapedSpaceOnAuthority())
            .addNormalizer(new OptimisticDnsScheme())
            .addNormalizer(new CheckLongEnough());

    public static final UriBuilderConfig SURT_KEY = new UriBuilderConfig()
            .parser(UriConfigs.WHATWG_PARSER)
            .referenceResolver(UriConfigs.WHATWG_REFERENCE_RESOLVER)
            .requireAbsoluteUri(true)
            .strictReferenceResolution(false)
            .caseNormalization(true)
            .percentEncodingNormalization(true)
            .percentEncodingCase(UriBuilderConfig.HexCase.UPPER)
            .normalizeIpv4(true)
            .normalizeIpv6(true)
            .ipv6Case(UriBuilderConfig.HexCase.UPPER)
            .pathSegmentNormalization(true)
            .schemeBasedNormalization(true)
            .encodeIllegalCharacters(true)
            .punycodeUnknownScheme(true)
            .defaultFormat(UriConfigs.SURT_KEY_FORMAT)
            .addNormalizer(new LaxTrimming())
            .addNormalizer(new StripWwwN())
            .addNormalizer(new StripSessionId())
            .addNormalizer(new StripErrorneousExtraSlashes())
            .addNormalizer(new StripSlashAtEndOfPath())
            .addNormalizer(new StripTrailingEscapedSpaceOnAuthority())
            .addNormalizer(new InferCommonSchemesForSchemelessUri())
            .addNormalizer(new OptimisticDnsScheme())
            .addNormalizer(new WhatwgPreTrimming())
            .addNormalizer(new WhatwgNormalizeHostAndPath())
            .addNormalizer(new RemoveLocalhost())
            .addNormalizer(new CheckLongEnough());

    public static final UriBuilderConfig LEGACY_SURT_KEY = new UriBuilderConfig()
            .parser(UriConfigs.LAX_PARSER)
            .referenceResolver(UriConfigs.DEFAULT_REFERENCE_RESOLVER)
            .requireAbsoluteUri(true)
            .strictReferenceResolution(false)
            .caseNormalization(true)
            .percentEncodingNormalization(true)
            .percentEncodingCase(UriBuilderConfig.HexCase.UPPER)
            .normalizeIpv4(true)
            .normalizeIpv6(true)
            .ipv6Case(UriBuilderConfig.HexCase.UPPER)
            .pathSegmentNormalization(true)
            .schemeBasedNormalization(true)
            .encodeIllegalCharacters(true)
            // Consider URIs too long for IE as illegal.
            .maxUriLength(2083)
            .defaultFormat(UriConfigs.LEGACY_SURT_KEY_FORMAT)
            .addNormalizer(new LaxTrimming())
            .addNormalizer(new AllLowerCase())
            .addNormalizer(new StripWwwN())
            .addNormalizer(new StripSessionId())
            .addNormalizer(new StripErrorneousExtraSlashes())
            .addNormalizer(new StripSlashAtEndOfPath())
            .addNormalizer(new StripTrailingEscapedSpaceOnAuthority())
            .addNormalizer(new InferCommonSchemesForSchemelessUri())
            .addNormalizer(new OptimisticDnsScheme())
            .addNormalizer(new CheckLongEnough());

    /**
     * Avoid instantiation.
     */
    private UriConfigs() {
    }

}