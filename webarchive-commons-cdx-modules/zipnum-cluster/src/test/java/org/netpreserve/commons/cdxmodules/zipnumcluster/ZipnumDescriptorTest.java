/*
 * Copyright 2015 IIPC.
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

import org.netpreserve.commons.cdxmodules.zipnumcluster.ZipnumDescriptor;
import org.netpreserve.commons.cdx.SearchKeyTemplate;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.netpreserve.commons.cdx.SearchKey;
import org.netpreserve.commons.cdx.cdxrecord.CdxLineFormat;
import org.netpreserve.commons.cdx.cdxsource.SourceBlock;

import static org.assertj.core.api.Assertions.*;

/**
 *
 */
public class ZipnumDescriptorTest {

    /**
     * Test of calculateBlocks method, of class ZipnumDescriptor.
     */
    @Test
    @Ignore
    public void testCalculateBlocks() {
        Path path = Paths.get("/media/ssd1/docker/openwayback/data/zipnum");
        ZipnumDescriptor descriptor = new ZipnumDescriptor(path, CdxLineFormat.CDX11LINE);

        SourceBlock expectedBlock1 = new SourceBlock(
                "69,148,159,195)/output/arcweb_norge_n20_map2315255483446.png 20070904170102",
                288011, 142674, "cdx-00000.gz");

        SearchKey key = new SearchKeyTemplate().uriRange("ac", "biz").createSearchKey(CdxLineFormat.CDX11LINE);
        List<SourceBlock> blocks = descriptor.calculateBlocks(key);

        assertThat(blocks)
                .hasSize(1)
                .containsExactly(expectedBlock1);

    }

    /**
     * Test of read method, of class ZipnumDescriptor.
     */
    @Test
    @Ignore
    public void testRead() throws IOException {
        Path path = Paths.get("/media/ssd1/docker/openwayback/data/zipnum");
        ZipnumDescriptor descriptor = new ZipnumDescriptor(path, CdxLineFormat.CDX11LINE);

        SourceBlock expectedBlock1 = new SourceBlock(
                "69,148,159,195)/output/arcweb_norge_n20_map2315255483446.png 20070904170102",
                288011, 142674, "cdx-00000.gz");

        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        descriptor.read(expectedBlock1, buffer);
        System.out.println(new String(buffer.array(), buffer.arrayOffset(), buffer.limit()));

        assertThat(1).isNegative();
    }

}
