package kr.bydelta

import scala.annotation.tailrec

/**
  * <h3>통합 Scala 한국어 분석기, Koala</h3>
  *
  * <p>각 Package의 Parser와 Tagger를 참조하세요.</p>
  */
package object koala {

  /** '가' 위치 **/
  final val HANGUL_START: Int = '가'
  /** '힣' 위치 **/
  final val HANGUL_END: Int = '힣'
  /** 종성 범위 **/
  final val JONGSUNG_RANGE = 0x001C
  /** 중성 범위 **/
  final val JUNGSUNG_RANGE = 0x024C
  protected[koala] final val ALPHA_PRON = Seq(
    "에이치", "더블유", "에이", "에프", "아이", "제이", "케이",
    "에스", "브이", "엑스", "와이", "제트", "비", "씨", "디",
    "이", "지", "엘", "엠", "엔", "오", "피", "큐", "알",
    "티", "유"
  )
  protected[koala] final val ALPHA_PRON_ORDER = Seq(
    'H', 'W', 'A', 'F', 'I', 'J', 'K', 'S', 'V', 'X', 'Y', 'Z',
    'B', 'C', 'D', 'E', 'G', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'T', 'U'
  )

  /**
    * 통합 의존구문분석표기로 변경.
    *
    * @param tag 통합표기로 변경할 의존구문분석 결과.
    * @return 통합표기.
    */
  def KKMAdepTag(tag: String): FunctionalTag.Value = {
    tag match {
      case "목적어" | "(주어,목적)대상" => FunctionalTag.Object
      case "주어" => FunctionalTag.Subject
      case "부사어" | "이유" => FunctionalTag.Adjunct
      case "보어" | "인용" => FunctionalTag.Complement
      case "수식" => FunctionalTag.Modifier
      case "동일" | "보조 연결" | "의존 연결" | "대등 연결" | "체언 연결" | "연결" => FunctionalTag.Conjunctive
      case _ =>
        FunctionalTag.Undefined
    }
  }

  /**
    * 통합 의존구문분석표기로 변경.
    *
    * @param tag 통합표기로 변경할 의존구문분석 결과.
    * @return 통합표기.
    */
  def HNNdepTag(tag: String): FunctionalTag.Value = {
    tag match {
      case "SBJ" => FunctionalTag.Subject
      case "OBJ" => FunctionalTag.Object
      case "CMP" => FunctionalTag.Complement
      case "VMOD" | "NMOD" | "MOD" => FunctionalTag.Modifier
      case "ADV" | "AJT" => FunctionalTag.Adjunct
      case "CNJ" => FunctionalTag.Conjunctive
      case "INT" => FunctionalTag.Interjective
      case "PRN" => FunctionalTag.Parenthetical
      case _ => FunctionalTag.Undefined
    }
  }

  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def tagToKomoran(tag: _root_.kr.bydelta.koala.POS.Value): String = {
    tag match {
      case POS.NNM => "NNB"
      case POS.XSM | POS.XSO => "XSN"
      case POS.XPV => "XR"
      case POS.SY => "SW"
      case POS.UN | POS.UV | POS.UE => "NA"
      case _ => tag.toString
    }
  }

  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def tagToTwt(tag: _root_.kr.bydelta.koala.POS.Value): String = {
    tag match {
      case POS.NNG | POS.NNB |
           POS.NNM | POS.NP => "Noun"
      case POS.NNP => "ProperNoun"
      case POS.NR | POS.SN => "Number"
      case POS.VV | POS.VX |
           POS.VCP | POS.VCN => "Verb"
      case POS.VA => "Adjective"
      case POS.MM => "Determiner"
      case POS.MAG | POS.MAJ => "Adverb"
      case POS.IC => "Exclamation"
      case POS.JKB | POS.JKC |
           POS.JKG | POS.JKO |
           POS.JKQ | POS.JKS |
           POS.JKV | POS.JX => "Josa"
      case POS.JC => "Conjunction"
      case POS.EP => "PreEomi"
      case POS.EF | POS.EC |
           POS.ETM | POS.ETN => "Eomi"
      case POS.XPN => "NounPrefix"
      case POS.XPV => "VerbPrefix"
      case POS.XSA | POS.XSM |
           POS.XSN | POS.XSO | POS.XSV => "Suffix"
      case POS.SF => "Punctuation"
      case POS.SS | POS.SP |
           POS.SE | POS.SY | POS.XR => "Others"
      case POS.UE | POS.UN | POS.UV => "Unknown"
      case POS.SL => "Foreign"
    }
  }

  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def tagToEunjeon(tag: _root_.kr.bydelta.koala.POS.Value): String = {
    tag match {
      case POS.NNM => "NNBC"
      case POS.SS => "SSO"
      case POS.SP => "SC"
      case POS.XPV => "XR"
      case POS.XSM | POS.XSO => "XSN"
      case POS.UN | POS.UE | POS.UV => "UNKNOWN"
      case x => x.toString
    }
  }

  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def tagToHNN(tag: _root_.kr.bydelta.koala.POS.Value): String = {
    (tag match {
      case POS.NNG | POS.UN => "NCN"
      case POS.NNP => "NQQ"
      case POS.NNB => "NBN"
      case POS.NNM => "NBU"
      case POS.NR => "NNC"
      case POS.NP => "NPD"
      case POS.VV | POS.UV => "PVG"
      case POS.VA => "PAA"
      case POS.VX => "PX"
      case POS.VCP => "JP"
      case POS.VCN => "PAA"
      case POS.MM => "MMA"
      case POS.IC => "II"
      case POS.JKS => "JCS"
      case POS.JKC => "JCC"
      case POS.JKG => "JCM"
      case POS.JKO => "JCO"
      case POS.JKB => "JCA"
      case POS.JKV => "JCV"
      case POS.JKQ => "JCR"
      case POS.JC => "JCT"
      case POS.JX => "JXF"
      case POS.EC => "ECC"
      case POS.XSO | POS.XSN => "XSNX"
      case POS.XSV => "XSVN"
      case POS.XSA => "XSMN"
      case POS.XSM => "XSAS"
      case POS.SS => "SL"
      case POS.SL => "F"
      case POS.SN => "NNC"
      case POS.SY | POS.XR | POS.UE => "SY"
      case POS.XPN | POS.XPV => "XP"
      case x => x.toString
    }).toLowerCase
  }

  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def tagToKKMA(tag: _root_.kr.bydelta.koala.POS.Value): String = {
    tag match {
      case POS.VX => "VXV"
      case POS.MM => "MDT"
      case POS.MAJ => "MAC"
      case POS.JKB => "JKM"
      case POS.JKV => "JKI"
      case POS.EP => "EPT"
      case POS.EF => "EFN"
      case POS.EC => "ECE"
      case POS.ETM => "ETD"
      case POS.SY => "SW"
      case POS.SL => "OL"
      case POS.SN => "ON"
      case x => x.toString
    }
  }

  /**
    * 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def fromKomoranTag(tag: String): POS.Value = {
    tag match {
      case "SW" | "SO" => POS.SY
      case "NA" => POS.UE
      case "SL" | "SH" => POS.SL
      case _ => POS withName tag
    }
  }

  /**
    * 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def fromTwtTag(tag: String): POS.Value = {
    tag match {
      case "Noun" => POS.NNG
      case "ProperNoun" => POS.NNP
      case "Number" => POS.NR
      case "Verb" => POS.VV
      case "Adjective" => POS.VA
      case "Determiner" => POS.MM
      case "Adverb" => POS.MAG
      case "Exclamation" => POS.IC
      case "Josa" => POS.JX
      case "Conjunction" => POS.JC
      case "PreEomi" => POS.EP
      case "Eomi" => POS.EF
      case "NounPrefix" => POS.XPN
      case "VerbPrefix" => POS.XPV
      case "Suffix" => POS.XSO
      case "Punctuation" => POS.SF
      case "Unknown" => POS.UE
      case "Foreign" | "Alpha" => POS.SL
      case _ => POS.SY
    }
  }

  /**
    * 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def fromEunjeonTag(tag: String): POS.Value = {
    tag.toUpperCase match {
      case "NNBC" => POS.NNM
      case "SC" => POS.SP
      case "SSO" | "SSC" => POS.SS
      case "SL" | "SH" => POS.SL
      case "UNKNOWN" => POS.UE
      case x => POS withName x
    }
  }

  /**
    * 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def fromHNNTag(tag: String): POS.Value = {
    tag match {
      case x if x startsWith "nc" => POS.NNG
      case x if x startsWith "nq" => POS.NNP
      case "nbn" | "nbs" => POS.NNB
      case "nbu" => POS.NNM
      case x if x startsWith "nn" => POS.NR
      case x if x startsWith "np" => POS.NP
      case x if x startsWith "pv" => POS.VV
      case x if x startsWith "pa" => POS.VA
      case "px" => POS.VX
      case "jp" => POS.VCP
      case x if x startsWith "mm" => POS.MM
      case "mad" | "mag" => POS.MAG
      case "maj" => POS.MAJ
      case "ii" => POS.IC
      case "jcs" => POS.JKS
      case "jcc" => POS.JKC
      case "jcm" => POS.JKG
      case "jco" => POS.JKO
      case "jca" => POS.JKB
      case "jcv" => POS.JKV
      case "jcr" => POS.JKQ
      case "jct" | "jcj" => POS.JC
      case "jxc" | "jxf" => POS.JX
      case x if x startsWith "ec" => POS.EC
      case "xp" => POS.XPN
      case x if x startsWith "xsn" => POS.XSN
      case x if x startsWith "xsv" => POS.XSV
      case x if x startsWith "xsm" => POS.XSA
      case x if x startsWith "xsa" => POS.XSM
      case "sl" | "sr" => POS.SS
      case "sd" | "su" | "sy" => POS.SY
      case "f" => POS.SL
      case x => POS withName x.toUpperCase
    }
  }

  /**
    * 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def fromKKMATag(tag: String): POS.Value = {
    tag.toUpperCase match {
      case "VXV" | "VXA" => POS.VX
      case "MDT" | "MDN" => POS.MM
      case "MAC" => POS.MAJ
      case "JKM" => POS.JKB
      case "JKI" => POS.JKV
      case x if x startsWith "EP" => POS.EP
      case x if x startsWith "EF" => POS.EF
      case x if x startsWith "EC" => POS.EC
      case "ETD" => POS.ETM
      case "SO" | "SW" => POS.SY
      case "OL" | "OH" => POS.SL
      case "ON" => POS.SN
      case "EMO" => POS.SY //Emoticons
      case x => POS withName x
    }
  }

  @tailrec
  final def pronounceAlphabet(x: String, acc: String = ""): String =
    if (x.isEmpty) acc
    else {
      pronounceAlphabet(x.tail, acc + ALPHA_PRON(ALPHA_PRON_ORDER.indexOf(x.head)))
    }

  @tailrec
  final def writeAlphabet(x: String, acc: String = ""): String =
    if (x.isEmpty) acc
    else {
      ALPHA_PRON.indexWhere(x.startsWith) match {
        case -1 => acc + x
        case ind => writeAlphabet(x.substring(ALPHA_PRON(ind).length), acc + ALPHA_PRON_ORDER(ind))
      }
    }

  @tailrec
  final def isAlphabetPronounced(word: String): Boolean =
    if (word.isEmpty) true
    else {
      ALPHA_PRON.find(word.startsWith) match {
        case Some(p) => isAlphabetPronounced(word.substring(p.length))
        case None => false
      }
    }

  def reconstructKorean(cho: Int = 11, jung: Int = 0, jong: Int = 0): Char =
    (HANGUL_START + cho * JUNGSUNG_RANGE + jung * JONGSUNG_RANGE + jong).toChar

  /**
    * 한국어를 구성하는 문자의 연산.
    *
    * @param ch 검사할 문자.
    */
  implicit class KoreanCharacterExtension(ch: Char) {
    /**
      * (Code modified from Seunjeon package)
      * 종성으로 끝나는지 확인.
      *
      * @return 종성으로 끝난다면, true를, 없다면 false를 반환.
      */
    def endsWithJongsung = getJongsungCode != 0

    /**
      * (Code modified from Seunjeon package)
      * 종성 종료 코드
      *
      * @return 종성으로 끝난다면, 해당 위치를, 없다면 0을 반환.
      */
    def getJongsungCode = (ch - HANGUL_START) % JONGSUNG_RANGE

    /**
      * (Code modified from Seunjeon package)
      * 중성 종료 코드
      *
      * @return 중성으로 끝난다면, 해당 위치를, 없다면 0을 반환.
      */
    def getJungsungCode = (ch - HANGUL_START) % JUNGSUNG_RANGE / JONGSUNG_RANGE

    /**
      * (Code modified from Seunjeon package)
      * 초성 종료 코드
      *
      * @return 초성으로 끝난다면, 해당 위치를, 없다면 0을 반환.
      */
    def getChosungCode = (ch - HANGUL_START) / JUNGSUNG_RANGE

    /**
      * (Code modified from Seunjeon package)
      * 한글 문자로 끝나는 지 확인.
      *
      * @return True: 종료 문자가 한글일 경우.
      */
    def isHangul = {
      ((HANGUL_START <= ch && ch <= HANGUL_END)
        || (0x1100 <= ch && ch <= 0x11FF)
        || (0x3130 <= ch && ch <= 0x318F))
    }

    /**
      * (Code modified from Seunjeon package)
      * 한글의 불완전 문자인지 확인
      *
      * @return True: 종료 문자가 한글일 경우.
      */
    def isIncompleteHangul = {
      ((0x1100 <= ch && ch <= 0x11FF)
        || (0x3130 <= ch && ch <= 0x318F))
    }

    /**
      * 완성된 문자의 범위인지 확인.
      *
      * @return True: 완성된 문자일 경우.
      */
    def isCompleteHangul = HANGUL_START <= ch && ch <= HANGUL_END
  }

  /**
    * 한국어를 구성하는 문자열의 연산.
    *
    * @param word 검사할 문자열.
    */
  implicit class KoreanStringExtension(word: String) {
    /**
      * (Code modified from Seunjeon package)
      * 종성으로 끝나는지 확인.
      *
      * @return 종성으로 끝난다면, true를, 없다면 false를 반환.
      */
    def endsWithJongsung = word.last.endsWithJongsung

    /**
      * (Code modified from Seunjeon package)
      * 한글 문자로 끝나는 지 확인.
      *
      * @return True: 종료 문자가 한글일 경우.
      */
    def endsWithHangul = word.last.isHangul

    def filterNonHangul = {
      word.replaceAll("(?U)[^가-힣\\s]+", " ").trim.split("(?U)\\s+").toSeq
    }
  }
}