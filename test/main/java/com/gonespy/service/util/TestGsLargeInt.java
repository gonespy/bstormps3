package com.gonespy.service.util;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;

// NOTE: all functions tested against C except where specified
public class TestGsLargeInt {

    @Test
    public void testGsLargeIntSetFromHexStringShorter() {
        String hex = "1";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        assertThat(s.length).isEqualTo(1);
        assertThat(Arrays.copyOfRange(s.data,0, s.length)).asList().isEqualTo(ImmutableList.of(1L));
    }

    @Test
    public void testGsLargeIntSetFromHexStringShort() {
        String hex = "01f";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        assertThat(s.length).isEqualTo(1);
        assertThat(Arrays.copyOfRange(s.data,0, s.length)).asList().isEqualTo(ImmutableList.of(31L));
    }

    @Test
    public void testGsLargeIntSetFromHexStringShort2() {
        String hex = "01ff";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        assertThat(s.length).isEqualTo(1);
        assertThat(Arrays.copyOfRange(s.data,0, s.length)).asList().isEqualTo(ImmutableList.of(511L));
    }

    @Test
    public void testGsLargeIntSetFromHexStringLong() {
        String hex = "01234567890123456789";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        assertThat(s.length).isEqualTo(3);
        assertThat(Arrays.copyOfRange(s.data,0, s.length)).asList().isEqualTo(ImmutableList.of(591751049L, 1164413185L, 291L));
    }

    @Test
    public void testGsLargeIntSetFromHexStringManyFs() {
        String hex = "ffffffffffffffffffff";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        assertThat(s.length).isEqualTo(3);
        assertThat(Arrays.copyOfRange(s.data,0, s.length)).asList().isEqualTo(ImmutableList.of(4294967295L, 4294967295L, 65535L));
    }

    @Test
    public void testGsLargeIntSetFromHexString256Chars() {
        String hex = "95375465E3FAC4900FC912E7B30EF7171B0546DF4D185DB04F21C79153CE091859DF2EBDDFE5047D80C2EF86A2169B05A933AE2EAB2962F7B32CFE3CB0C25E7E3A26BB6534C9CF19640F1143735BD0CEAA7AA88CD64ACEC6EEB037007567F1EC51D00C1D2F1FFCFECB5300C93D6D6A50C1E3BDF495FC17601794E5655C476819";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        assertThat(s.length).isEqualTo(32);
        assertThat(Arrays.copyOfRange(s.data, 0, s.length)).asList().isEqualTo(ImmutableList.of(1548183577L,395634021L,2516326240L,3252927988L,1030580816L,3411214537L,790625534L,1372589085L,1969746412L,4004525824L,3595226822L,2860165260L,1935397070L,1678709059L,885640985L,975616869L,2965528190L,3006070332L,2871616247L,2838736430L,2719390469L,2160258950L,3756328061L,1507798717L,1406011672L,1327613841L,1293442480L,453330655L,3004102423L,264835815L,3824862352L,2503431269L));
    }

    @Test
    public void testGsLargeIntReverseBytesOneLength() {
        GsLargeInt s = new GsLargeInt();
        s.length = 1;
        s.data[0] = 511L;
        s.gsLargeIntReverseBytes();
        assertThat(s.length).isEqualTo(1);
        assertThat(Arrays.copyOfRange(s.data,0, s.length)).asList().isEqualTo(ImmutableList.of(511L));
    }

    @Test
    public void testGsLargeIntReverseBytesZeroLength() {
        GsLargeInt s = new GsLargeInt();
        s.length = 0;
        s.gsLargeIntReverseBytes();
        assertThat(s.length).isEqualTo(0);
    }

    @Test
    public void testGsLargeIntReverseBytesOdd() {
        GsLargeInt s = new GsLargeInt();
        s.length = 3;
        s.data[0] = 1L;
        s.data[1] = 2L;
        s.data[2] = 3L;
        s.gsLargeIntReverseBytes();
        assertThat(s.length).isEqualTo(3);
        assertThat(Arrays.copyOfRange(s.data,0, s.length)).asList().isEqualTo(ImmutableList.of(3L, 2L, 1L));
    }

    @Test
    public void testGsLargeIntReverseBytesEven() {
        GsLargeInt s = new GsLargeInt();
        s.length = 4;
        s.data[0] = 1L;
        s.data[1] = 2L;
        s.data[2] = 3L;
        s.data[3] = 4L;
        s.gsLargeIntReverseBytes();
        assertThat(s.length).isEqualTo(4);
        assertThat(Arrays.copyOfRange(s.data,0, s.length)).asList().isEqualTo(ImmutableList.of(4L, 3L, 2L, 1L));
    }

    @Test
    public void testGsLargeIntGetByteLengthZeroLength() {
        String hex = "";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        long byteLength = s.gsLargeIntGetByteLength();
        assertThat(byteLength).isEqualTo(0);
    }

    @Test
    public void testGsLargeIntGetByteLengthZero() {
        String hex = "0";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        long byteLength = s.gsLargeIntGetByteLength();
        assertThat(byteLength).isEqualTo(0);
    }

    @Test
    public void testGsLargeIntGetByteLengthShorter() {
        String hex = "1";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        long byteLength = s.gsLargeIntGetByteLength();
        assertThat(byteLength).isEqualTo(1);
    }

    @Test
    public void testGsLargeIntGetByteLengthShort() {
        String hex = "01f";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        long byteLength = s.gsLargeIntGetByteLength();
        assertThat(byteLength).isEqualTo(1);
    }

    @Test
    public void testGsLargeIntGetByteLengthShort22() {
        String hex = "10f";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        long byteLength = s.gsLargeIntGetByteLength();
        assertThat(byteLength).isEqualTo(2);
    }

    @Test
    public void testGsLargeIntGetByteLengthShort2() {
        String hex = "01ff";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        long byteLength = s.gsLargeIntGetByteLength();
        assertThat(byteLength).isEqualTo(2);
    }

    @Test
    public void testGsLargeIntGetByteLengthLong() {
        String hex = "01234567890123456789";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        long byteLength = s.gsLargeIntGetByteLength();
        assertThat(byteLength).isEqualTo(10);
    }

    @Test
    public void testGsLargeIntGetByteLengthManyFs() {
        String hex = "ffffffffffffffffffff";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        long byteLength = s.gsLargeIntGetByteLength();
        assertThat(byteLength).isEqualTo(10);
    }

    @Test
    public void testGsLargeIntGetByteLength256Chars() {
        String hex = "95375465E3FAC4900FC912E7B30EF7171B0546DF4D185DB04F21C79153CE091859DF2EBDDFE5047D80C2EF86A2169B05A933AE2EAB2962F7B32CFE3CB0C25E7E3A26BB6534C9CF19640F1143735BD0CEAA7AA88CD64ACEC6EEB037007567F1EC51D00C1D2F1FFCFECB5300C93D6D6A50C1E3BDF495FC17601794E5655C476819";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        long byteLength = s.gsLargeIntGetByteLength();
        assertThat(byteLength).isEqualTo(128);
    }

    @Test
    // TODO: more tests
    // TODO: test against C?
    public void testDataAsByteArray() {
        String hex = "1";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        byte[] bytes = s.dataAsByteArray();
        assertThat(bytes).asList().hasSize(4);
    }

    @Test
    public void testToHexStringFourBytes() {
        String hex = "00070001";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        String hexAgain = s.toHexString();
        assertThat(hexAgain).isEqualTo(hex);
    }

    @Test
    public void testToHexString128Bytes() {
        String hex = "95375465E3FAC4900FC912E7B30EF7171B0546DF4D185DB04F21C79153CE091859DF2EBDDFE5047D80C2EF86A2169B05A933AE2EAB2962F7B32CFE3CB0C25E7E3A26BB6534C9CF19640F1143735BD0CEAA7AA88CD64ACEC6EEB037007567F1EC51D00C1D2F1FFCFECB5300C93D6D6A50C1E3BDF495FC17601794E5655C476819";
        GsLargeInt s = GsLargeInt.gsLargeIntSetFromHexString(hex);
        String hexAgain = s.toHexString();
        assertThat(hexAgain).isEqualTo(hex);
    }

}
