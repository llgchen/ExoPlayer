/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.google.android.exoplayer2.text.webvtt;

import static com.google.android.exoplayer2.testutil.truth.SpannedSubject.assertThat;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.android.exoplayer2.testutil.TestUtil;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.ColorParser;
import com.google.common.collect.Iterables;
import com.google.common.truth.Expect;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link WebvttDecoder}. */
@RunWith(AndroidJUnit4.class)
public class WebvttDecoderTest {

  private static final String TYPICAL_FILE = "webvtt/typical";
  private static final String TYPICAL_WITH_BAD_TIMESTAMPS = "webvtt/typical_with_bad_timestamps";
  private static final String TYPICAL_WITH_IDS_FILE = "webvtt/typical_with_identifiers";
  private static final String TYPICAL_WITH_COMMENTS_FILE = "webvtt/typical_with_comments";
  private static final String WITH_POSITIONING_FILE = "webvtt/with_positioning";
  private static final String WITH_VERTICAL_FILE = "webvtt/with_vertical";
  private static final String WITH_BAD_CUE_HEADER_FILE = "webvtt/with_bad_cue_header";
  private static final String WITH_TAGS_FILE = "webvtt/with_tags";
  private static final String WITH_CSS_STYLES = "webvtt/with_css_styles";
  private static final String WITH_CSS_COMPLEX_SELECTORS = "webvtt/with_css_complex_selectors";
  private static final String WITH_CSS_TEXT_COMBINE_UPRIGHT =
      "webvtt/with_css_text_combine_upright";
  private static final String WITH_BOM = "webvtt/with_bom";
  private static final String EMPTY_FILE = "webvtt/empty";

  @Rule public final Expect expect = Expect.create();

  @Test
  public void testDecodeEmpty() throws IOException {
    WebvttDecoder decoder = new WebvttDecoder();
    byte[] bytes = TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), EMPTY_FILE);
    try {
      decoder.decode(bytes, bytes.length, /* reset= */ false);
      fail();
    } catch (SubtitleDecoderException expected) {
      // Do nothing.
    }
  }

  @Test
  public void testDecodeTypical() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(TYPICAL_FILE);

    assertThat(subtitle.getEventTimeCount()).isEqualTo(4);

    assertThat(subtitle.getEventTime(0)).isEqualTo(0L);
    assertThat(subtitle.getEventTime(1)).isEqualTo(1_234_000L);
    Cue firstCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(0)));
    assertThat(firstCue.text.toString()).isEqualTo("This is the first subtitle.");

    assertThat(subtitle.getEventTime(2)).isEqualTo(2_345_000L);
    assertThat(subtitle.getEventTime(3)).isEqualTo(3_456_000L);
    Cue secondCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(2)));
    assertThat(secondCue.text.toString()).isEqualTo("This is the second subtitle.");
  }

  @Test
  public void testDecodeWithBom() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(WITH_BOM);

    assertThat(subtitle.getEventTimeCount()).isEqualTo(4);

    assertThat(subtitle.getEventTime(0)).isEqualTo(0L);
    assertThat(subtitle.getEventTime(1)).isEqualTo(1_234_000L);
    Cue firstCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(0)));
    assertThat(firstCue.text.toString()).isEqualTo("This is the first subtitle.");

    assertThat(subtitle.getEventTime(2)).isEqualTo(2_345_000L);
    assertThat(subtitle.getEventTime(3)).isEqualTo(3_456_000L);
    Cue secondCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(2)));
    assertThat(secondCue.text.toString()).isEqualTo("This is the second subtitle.");
  }

  @Test
  public void testDecodeTypicalWithBadTimestamps() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(TYPICAL_WITH_BAD_TIMESTAMPS);

    assertThat(subtitle.getEventTimeCount()).isEqualTo(4);

    assertThat(subtitle.getEventTime(0)).isEqualTo(0L);
    assertThat(subtitle.getEventTime(1)).isEqualTo(1_234_000L);
    Cue firstCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(0)));
    assertThat(firstCue.text.toString()).isEqualTo("This is the first subtitle.");

    assertThat(subtitle.getEventTime(2)).isEqualTo(2_345_000L);
    assertThat(subtitle.getEventTime(3)).isEqualTo(3_456_000L);
    Cue secondCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(2)));
    assertThat(secondCue.text.toString()).isEqualTo("This is the second subtitle.");
  }

  @Test
  public void testDecodeTypicalWithIds() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(TYPICAL_WITH_IDS_FILE);

    assertThat(subtitle.getEventTimeCount()).isEqualTo(4);

    assertThat(subtitle.getEventTime(0)).isEqualTo(0L);
    assertThat(subtitle.getEventTime(1)).isEqualTo(1_234_000L);
    Cue firstCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(0)));
    assertThat(firstCue.text.toString()).isEqualTo("This is the first subtitle.");

    assertThat(subtitle.getEventTime(2)).isEqualTo(2_345_000L);
    assertThat(subtitle.getEventTime(3)).isEqualTo(3_456_000L);
    Cue secondCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(2)));
    assertThat(secondCue.text.toString()).isEqualTo("This is the second subtitle.");
  }

  @Test
  public void testDecodeTypicalWithComments() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(TYPICAL_WITH_COMMENTS_FILE);

    assertThat(subtitle.getEventTimeCount()).isEqualTo(4);

    assertThat(subtitle.getEventTime(0)).isEqualTo(0L);
    assertThat(subtitle.getEventTime(0 + 1)).isEqualTo(1_234_000L);
    Cue firstCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(0)));
    assertThat(firstCue.text.toString()).isEqualTo("This is the first subtitle.");

    assertThat(subtitle.getEventTime(2)).isEqualTo(2_345_000L);
    assertThat(subtitle.getEventTime(2 + 1)).isEqualTo(3_456_000L);
    Cue secondCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(2)));
    assertThat(secondCue.text.toString()).isEqualTo("This is the second subtitle.");
  }

  @Test
  public void testDecodeWithTags() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(WITH_TAGS_FILE);

    assertThat(subtitle.getEventTimeCount()).isEqualTo(8);

    assertThat(subtitle.getEventTime(0)).isEqualTo(0L);
    assertThat(subtitle.getEventTime(1)).isEqualTo(1_234_000L);
    Cue firstCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(0)));
    assertThat(firstCue.text.toString()).isEqualTo("This is the first subtitle.");

    assertThat(subtitle.getEventTime(2)).isEqualTo(2_345_000L);
    assertThat(subtitle.getEventTime(3)).isEqualTo(3_456_000L);
    Cue secondCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(2)));
    assertThat(secondCue.text.toString()).isEqualTo("This is the second subtitle.");

    assertThat(subtitle.getEventTime(4)).isEqualTo(4_000_000L);
    assertThat(subtitle.getEventTime(5)).isEqualTo(5_000_000L);
    Cue thirdCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(4)));
    assertThat(thirdCue.text.toString()).isEqualTo("This is the third subtitle.");

    assertThat(subtitle.getEventTime(6)).isEqualTo(6_000_000L);
    assertThat(subtitle.getEventTime(7)).isEqualTo(7_000_000L);
    Cue fourthCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(6)));
    assertThat(fourthCue.text.toString()).isEqualTo("This is the <fourth> &subtitle.");
  }

  @Test
  public void testDecodeWithPositioning() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(WITH_POSITIONING_FILE);

    assertThat(subtitle.getEventTimeCount()).isEqualTo(12);

    assertThat(subtitle.getEventTime(0)).isEqualTo(0L);
    assertThat(subtitle.getEventTime(1)).isEqualTo(1_234_000L);
    Cue firstCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(0)));
    assertThat(firstCue.text.toString()).isEqualTo("This is the first subtitle.");
    assertThat(firstCue.position).isEqualTo(0.1f);
    assertThat(firstCue.positionAnchor).isEqualTo(Cue.ANCHOR_TYPE_START);
    assertThat(firstCue.textAlignment).isEqualTo(Alignment.ALIGN_NORMAL);
    assertThat(firstCue.size).isEqualTo(0.35f);
    // Unspecified values should use WebVTT defaults
    assertThat(firstCue.line).isEqualTo(Cue.DIMEN_UNSET);
    assertThat(firstCue.lineType).isEqualTo(Cue.LINE_TYPE_NUMBER);
    assertThat(firstCue.verticalType).isEqualTo(Cue.TYPE_UNSET);

    assertThat(subtitle.getEventTime(2)).isEqualTo(2_345_000L);
    assertThat(subtitle.getEventTime(3)).isEqualTo(3_456_000L);
    Cue secondCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(2)));
    assertThat(secondCue.text.toString()).isEqualTo("This is the second subtitle.");
    // Position is invalid so defaults to 0.5
    assertThat(secondCue.position).isEqualTo(0.5f);
    assertThat(secondCue.textAlignment).isEqualTo(Alignment.ALIGN_OPPOSITE);

    assertThat(subtitle.getEventTime(4)).isEqualTo(4_000_000L);
    assertThat(subtitle.getEventTime(5)).isEqualTo(5_000_000L);
    Cue thirdCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(4)));
    assertThat(thirdCue.text.toString()).isEqualTo("This is the third subtitle.");
    assertThat(thirdCue.line).isEqualTo(0.45f);
    assertThat(thirdCue.lineType).isEqualTo(Cue.LINE_TYPE_FRACTION);
    assertThat(thirdCue.lineAnchor).isEqualTo(Cue.ANCHOR_TYPE_END);
    assertThat(thirdCue.textAlignment).isEqualTo(Alignment.ALIGN_CENTER);
    // Derived from `align:middle`:
    assertThat(thirdCue.positionAnchor).isEqualTo(Cue.ANCHOR_TYPE_MIDDLE);

    assertThat(subtitle.getEventTime(6)).isEqualTo(6_000_000L);
    assertThat(subtitle.getEventTime(7)).isEqualTo(7_000_000L);
    Cue fourthCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(6)));
    assertThat(fourthCue.text.toString()).isEqualTo("This is the fourth subtitle.");
    assertThat(fourthCue.line).isEqualTo(-11f);
    assertThat(fourthCue.lineAnchor).isEqualTo(Cue.ANCHOR_TYPE_START);
    assertThat(fourthCue.textAlignment).isEqualTo(Alignment.ALIGN_CENTER);
    // Derived from `align:middle`:
    assertThat(fourthCue.position).isEqualTo(0.5f);
    assertThat(fourthCue.positionAnchor).isEqualTo(Cue.ANCHOR_TYPE_MIDDLE);

    assertThat(subtitle.getEventTime(8)).isEqualTo(8_000_000L);
    assertThat(subtitle.getEventTime(9)).isEqualTo(9_000_000L);
    Cue fifthCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(8)));
    assertThat(fifthCue.text.toString()).isEqualTo("This is the fifth subtitle.");
    assertThat(fifthCue.textAlignment).isEqualTo(Alignment.ALIGN_OPPOSITE);
    // Derived from `align:right`:
    assertThat(fifthCue.position).isEqualTo(1.0f);
    assertThat(fifthCue.positionAnchor).isEqualTo(Cue.ANCHOR_TYPE_END);

    assertThat(subtitle.getEventTime(10)).isEqualTo(10_000_000L);
    assertThat(subtitle.getEventTime(11)).isEqualTo(11_000_000L);
    Cue sixthCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(10)));
    assertThat(sixthCue.text.toString()).isEqualTo("This is the sixth subtitle.");
    assertThat(sixthCue.textAlignment).isEqualTo(Alignment.ALIGN_CENTER);
    // Derived from `align:center`:
    assertThat(sixthCue.position).isEqualTo(0.5f);
    assertThat(sixthCue.positionAnchor).isEqualTo(Cue.ANCHOR_TYPE_MIDDLE);
  }

  @Test
  public void testDecodeWithVertical() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(WITH_VERTICAL_FILE);

    assertThat(subtitle.getEventTimeCount()).isEqualTo(6);

    assertThat(subtitle.getEventTime(0)).isEqualTo(0L);
    assertThat(subtitle.getEventTime(1)).isEqualTo(1_234_000L);
    Cue firstCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(0)));
    assertThat(firstCue.text.toString()).isEqualTo("Vertical right-to-left (e.g. Japanese)");
    assertThat(firstCue.verticalType).isEqualTo(Cue.VERTICAL_TYPE_RL);

    assertThat(subtitle.getEventTime(2)).isEqualTo(2_345_000L);
    assertThat(subtitle.getEventTime(3)).isEqualTo(3_456_000L);
    Cue secondCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(2)));
    assertThat(secondCue.text.toString()).isEqualTo("Vertical left-to-right (e.g. Mongolian)");
    assertThat(secondCue.verticalType).isEqualTo(Cue.VERTICAL_TYPE_LR);

    assertThat(subtitle.getEventTime(4)).isEqualTo(4_000_000L);
    assertThat(subtitle.getEventTime(5)).isEqualTo(5_000_000L);
    Cue thirdCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(4)));
    assertThat(thirdCue.text.toString()).isEqualTo("No vertical setting (i.e. horizontal)");
    assertThat(thirdCue.verticalType).isEqualTo(Cue.TYPE_UNSET);
  }

  @Test
  public void testDecodeWithBadCueHeader() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(WITH_BAD_CUE_HEADER_FILE);

    assertThat(subtitle.getEventTimeCount()).isEqualTo(4);

    assertThat(subtitle.getEventTime(0)).isEqualTo(0L);
    assertThat(subtitle.getEventTime(1)).isEqualTo(1_234_000L);
    Cue firstCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(0)));
    assertThat(firstCue.text.toString()).isEqualTo("This is the first subtitle.");

    assertThat(subtitle.getEventTime(2)).isEqualTo(4_000_000L);
    assertThat(subtitle.getEventTime(3)).isEqualTo(5_000_000L);
    Cue secondCue = Iterables.getOnlyElement(subtitle.getCues(subtitle.getEventTime(2)));
    assertThat(secondCue.text.toString()).isEqualTo("This is the third subtitle.");
  }

  @Test
  public void testWebvttWithCssStyle() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(WITH_CSS_STYLES);

    Spanned firstCueText = getUniqueSpanTextAt(subtitle, 0);
    assertThat(firstCueText.toString()).isEqualTo("This is the first subtitle.");
    assertThat(firstCueText)
        .hasForegroundColorSpanBetween(0, firstCueText.length())
        .withColor(ColorParser.parseCssColor("papayawhip"));
    assertThat(firstCueText)
        .hasBackgroundColorSpanBetween(0, firstCueText.length())
        .withColor(ColorParser.parseCssColor("green"));

    Spanned secondCueText = getUniqueSpanTextAt(subtitle, 2_345_000);
    assertThat(secondCueText.toString()).isEqualTo("This is the second subtitle.");
    assertThat(secondCueText)
        .hasForegroundColorSpanBetween(0, secondCueText.length())
        .withColor(ColorParser.parseCssColor("peachpuff"));

    Spanned thirdCueText = getUniqueSpanTextAt(subtitle, 20_000_000);
    assertThat(thirdCueText.toString()).isEqualTo("This is a reference by element");
    assertThat(thirdCueText).hasUnderlineSpanBetween("This is a ".length(), thirdCueText.length());

    Spanned fourthCueText = getUniqueSpanTextAt(subtitle, 25_000_000);
    assertThat(fourthCueText.toString()).isEqualTo("You are an idiot\nYou don't have the guts");
    assertThat(fourthCueText)
        .hasBackgroundColorSpanBetween(0, "You are an idiot".length())
        .withColor(ColorParser.parseCssColor("lime"));
    assertThat(fourthCueText)
        .hasBoldSpanBetween("You are an idiot\n".length(), fourthCueText.length());
  }

  @Test
  public void testWithComplexCssSelectors() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(WITH_CSS_COMPLEX_SELECTORS);
    Spanned text = getUniqueSpanTextAt(subtitle, /* timeUs= */ 0);
    assertThat(text.getSpans(/* start= */ 30, text.length(), ForegroundColorSpan.class))
        .hasLength(1);
    assertThat(
            text.getSpans(/* start= */ 30, text.length(), ForegroundColorSpan.class)[0]
                .getForegroundColor())
        .isEqualTo(0xFFEE82EE);
    assertThat(text.getSpans(/* start= */ 30, text.length(), TypefaceSpan.class)).hasLength(1);
    assertThat(text.getSpans(/* start= */ 30, text.length(), TypefaceSpan.class)[0].getFamily())
        .isEqualTo("courier");

    text = getUniqueSpanTextAt(subtitle, /* timeUs= */ 2000000);
    assertThat(text.getSpans(/* start= */ 5, text.length(), TypefaceSpan.class)).hasLength(1);
    assertThat(text.getSpans(/* start= */ 5, text.length(), TypefaceSpan.class)[0].getFamily())
        .isEqualTo("courier");

    text = getUniqueSpanTextAt(subtitle, /* timeUs= */ 2500000);
    assertThat(text.getSpans(/* start= */ 5, text.length(), StyleSpan.class)).hasLength(1);
    assertThat(text.getSpans(/* start= */ 5, text.length(), StyleSpan.class)[0].getStyle())
        .isEqualTo(Typeface.BOLD);
    assertThat(text.getSpans(/* start= */ 5, text.length(), TypefaceSpan.class)).hasLength(1);
    assertThat(text.getSpans(/* start= */ 5, text.length(), TypefaceSpan.class)[0].getFamily())
        .isEqualTo("courier");

    text = getUniqueSpanTextAt(subtitle, /* timeUs= */ 4000000);
    assertThat(text.getSpans(/* start= */ 6, /* end= */ 22, StyleSpan.class)).hasLength(0);
    assertThat(text.getSpans(/* start= */ 30, text.length(), StyleSpan.class)).hasLength(1);
    assertThat(text.getSpans(/* start= */ 30, text.length(), StyleSpan.class)[0].getStyle())
        .isEqualTo(Typeface.BOLD);

    text = getUniqueSpanTextAt(subtitle, /* timeUs= */ 5000000);
    assertThat(text.getSpans(/* start= */ 9, /* end= */ 17, StyleSpan.class)).hasLength(0);
    assertThat(text.getSpans(/* start= */ 19, text.length(), StyleSpan.class)).hasLength(1);
    assertThat(text.getSpans(/* start= */ 19, text.length(), StyleSpan.class)[0].getStyle())
        .isEqualTo(Typeface.ITALIC);
  }

  @Test
  public void testWebvttWithCssTextCombineUpright() throws Exception {
    WebvttSubtitle subtitle = getSubtitleForTestAsset(WITH_CSS_TEXT_COMBINE_UPRIGHT);

    Spanned firstCueText = getUniqueSpanTextAt(subtitle, 500_000);
    assertThat(firstCueText)
        .hasHorizontalTextInVerticalContextSpanBetween("Combine ".length(), "Combine all".length());

    Spanned secondCueText = getUniqueSpanTextAt(subtitle, 3_500_000);
    assertThat(secondCueText)
        .hasHorizontalTextInVerticalContextSpanBetween(
            "Combine ".length(), "Combine 0004".length());
  }

  private WebvttSubtitle getSubtitleForTestAsset(String asset)
      throws IOException, SubtitleDecoderException {
    WebvttDecoder decoder = new WebvttDecoder();
    byte[] bytes = TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), asset);
    return (WebvttSubtitle) decoder.decode(bytes, bytes.length, /* reset= */ false);
  }

  private Spanned getUniqueSpanTextAt(WebvttSubtitle sub, long timeUs) {
    return (Spanned) sub.getCues(timeUs).get(0).text;
  }
}