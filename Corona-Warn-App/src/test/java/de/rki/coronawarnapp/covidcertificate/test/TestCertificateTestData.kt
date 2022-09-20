package de.rki.coronawarnapp.covidcertificate.test

import de.rki.coronawarnapp.covidcertificate.common.certificate.DccQrCodeExtractor
import de.rki.coronawarnapp.covidcertificate.test.core.qrcode.TestCertificateQRCode
import de.rki.coronawarnapp.covidcertificate.test.core.storage.TestCertificateContainer
import de.rki.coronawarnapp.covidcertificate.test.core.storage.types.GenericTestCertificateData
import de.rki.coronawarnapp.covidcertificate.test.core.storage.types.PCRCertificateData
import de.rki.coronawarnapp.covidcertificate.test.core.storage.types.RACertificateData
import de.rki.coronawarnapp.util.encryption.rsa.RSAKey
import okio.ByteString.Companion.decodeBase64
import java.time.Instant
import javax.inject.Inject

@Suppress("MaxLineLength")
class TestCertificateTestData @Inject constructor(
    private val qrCodeExtractor: DccQrCodeExtractor
) {

    val personATest1CertQRCodeString =
        "HC1:6BFQ$9FY7$\$Q00019C-5HQ57WDQTLUMS256TUMP49JLZCX/N4VB14NRINX6J7+KF\$UB.N83CMMPVNPAHJ035FT88%AJ1R*UOGR3ZWLC24+F7DO276HB.O*BEVJODVSKNOP0T4\$FZ.R/03N6QK3A05EQVBONNHSJ9WJT+B15H NBEZ3VI8-V77AFL4NY9V*JM *KOPC2H8PR5OG9:VIG$969FJRV28N9PORZJQ43EOIHIJ+83XDDRB201N.L58EB7 8GS8. 9GPNGHFV/6\$I3R3R4930TC/ZGZESM929.D59GO13E\$H7LMUGS18ABF6YD955C C6VPNMZU+3FJR1RVLBZDR2F*8A4MKWIH+/JPU1*H6E2CTHGYHPPU6U*FYFJ6RJO*I8P1T59S:V3V1SA86L8Y8A5XB*10112G8GHK77CBOK9QEI960TTNC.I3A4P8BM6DO:MI/98PC/ZP2:JW3JB.J:R2.5GX0J$1J.OOCYSS8A*DS\$AFVJR9RT-L6N%PS%CV%B8KL0%EL SXLBSA6 %M6JEN+E0Q8A:RAGW16KGUA627-Y2:EDH\$VFVT:29FWUTJSANSYCV4PSKNP\$ZV-8T27SV4CC3CJIV20TKBS22O$/MT/V*0Q0IUE0DBPT+UP03IR1CRFOP3"

    val personATest2CertQRCodeString =
        "HC1:6BFT$9W08+J2DO3L6COM9W\$B8MG9DIV*GZHTGBS46FQ5G+/N1D9K1LV-50OIETJPEU63TT MA5B-+B%95V72UUSRST4662Z8NME.249C1Y*5TIT+TI0QG*%9%VBYBOIZV7UNM%2A8P1N3+IDXN38BO%619B4D515FT*5CR4F:U8M/P-7T%TGX2CDDTB/5GR8:0I$ G*+N0-CIF51925USMI2WX4XKJA0G2XHP268P7ODSZYC4TKJOO:WQ437D:E78V-5UM4LGFBKZ80YIU 8%/GWAIM%KG/VIM2 M4BKP7NI/204CE9BSYP2JGI9GJGQUJ-UEYM7YAQR6E DHZ3-RM3:BQ3KRDMIV5.WVBJ8HT8DG7T GMJ47O2HSA+P6UFGUNIPCNOPFJIPWP8E 8BB0C08F 75N6UW20BA800 8LWN3D NV:U6XRDWF9FOQ1LT+L07BFUBZU37D8ZBP+FBKWO:X1ZK8.0V9-AX02UD5PP8K81+VO*.L4*DJRR7WPEL4G-LYCL+ 9\$TBHFNU0R%GD0/ESSOQH7PZD2 BJA3R74E4WG3W02B67EF8WZN7L0J2*3R5EG0VB7OM2I92W/UMJ7G1 FNSCF O 4W4VSO2DOJUF6I8%10-65UR/JJI3AN2EW-BLRVHQN77J"

    suspend fun personATest1CertQRCode() = qrCodeExtractor.extract(personATest1CertQRCodeString) as TestCertificateQRCode

    val personATest1StoredData = run {
        val publicKey = RSAKey.Public(
            "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEA2+WCCvy0SNqZMy/V1FYYMkBTGp/5BQt/NxUW1nIkj84u6duqNNQh4GjugoDc8epyl/yi3D61Jt7qArwk+eTcnW4/jEOexT5pCabRKrFm6IMndSefYrP3CeaD86ZU47uhnRuCG3TcPhIqUN2E37EbOsI9Z59JXc5tmmB71CxTF0bjE0PNLgbTU2snnsO6+oz/JLo7D2nw6E9yxSJ8JBjM5j+FC4sYLuO2nYi/BzAGZL/wsKrajg2hjA3f8r1cgst8HdzAJjMUG90pb3UG2K2KVRScbvF8pvRrzLCvJ/gqAGDXX/M00jr407vU8V4O2A9YdSavaC02iRFTNail65cbOW96p3ptjeejofj8l5PO5eBYWERla8NrlD9EcW93+aSmswn4w9iSSq+j38GMyhYulLcOlhKTeWumc5goDjcHyri48Ki70ddGzrxFxggaC/FqlCG85A6/43fVaWH/Wi2uPDPzaRGNQzXRy4LCuE/dvUzp8TlkpcT0QFy/Q4Ke0u1dAgMBAAE\u003d".decodeBase64()!!
        )
        val privateKey = RSAKey.Private(
            "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".decodeBase64()!!
        )
        PCRCertificateData(
            identifier = "identifier",
            registrationToken = "registrationToken",
            registeredAt = Instant.ofEpochMilli(12345),
            publicKeyRegisteredAt = Instant.ofEpochMilli(6789),
            rsaPublicKey = publicKey,
            rsaPrivateKey = privateKey,
            certificateReceivedAt = Instant.ofEpochMilli(123456789),
            encryptedDataEncryptionkey = "ZW5jcnlwdGVkRGF0YUVuY3J5cHRpb25rZXk=".decodeBase64()!!,
            testCertificateQrCode = personATest1CertQRCodeString,
        )
    }

    val personATest2StoredData = run {
        val publicKey = RSAKey.Public(
            "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEA2+WCCvy0SNqZMy/V1FYYMkBTGp/5BQt/NxUW1nIkj84u6duqNNQh4GjugoDc8epyl/yi3D61Jt7qArwk+eTcnW4/jEOexT5pCabRKrFm6IMndSefYrP3CeaD86ZU47uhnRuCG3TcPhIqUN2E37EbOsI9Z59JXc5tmmB71CxTF0bjE0PNLgbTU2snnsO6+oz/JLo7D2nw6E9yxSJ8JBjM5j+FC4sYLuO2nYi/BzAGZL/wsKrajg2hjA3f8r1cgst8HdzAJjMUG90pb3UG2K2KVRScbvF8pvRrzLCvJ/gqAGDXX/M00jr407vU8V4O2A9YdSavaC02iRFTNail65cbOW96p3ptjeejofj8l5PO5eBYWERla8NrlD9EcW93+aSmswn4w9iSSq+j38GMyhYulLcOlhKTeWumc5goDjcHyri48Ki70ddGzrxFxggaC/FqlCG85A6/43fVaWH/Wi2uPDPzaRGNQzXRy4LCuE/dvUzp8TlkpcT0QFy/Q4Ke0u1dAgMBAAE\u003d".decodeBase64()!!
        )
        val privateKey = RSAKey.Private(
            "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".decodeBase64()!!
        )
        PCRCertificateData(
            identifier = "identifier2",
            registrationToken = "registrationToken2",
            registeredAt = Instant.ofEpochMilli(123454),
            publicKeyRegisteredAt = Instant.ofEpochMilli(67389),
            rsaPublicKey = publicKey,
            rsaPrivateKey = privateKey,
            certificateReceivedAt = Instant.ofEpochMilli(123456789),
            encryptedDataEncryptionkey = "ZW5jcnlwdGVkRGF0YUVuY3J5cHRpb25rZXk=".decodeBase64()!!,
            testCertificateQrCode = personATest2CertQRCodeString,
        )
    }

    val personATest1Container = TestCertificateContainer(
        data = personATest1StoredData,
        qrCodeExtractor = qrCodeExtractor,
    )

    val personATest2CertStoredData = run {
        val publicKey = RSAKey.Public(
            "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAnrJ8PbmbOmEEmHB/8yg1bnkUT7jcF4Xfy2Me5imJgLVYQ0cL9UNP91cfFUrgMFV3fHOc0Uuay10TmrBLaEdzqDEZQH4Kj0uZ+hVCtzntVKFviuUoh08wxFlogtc5Sy0NhuTGyC1W/i2AX1SDvet2xMcc1fE44rITQEEAlG8+nfGpbppFHezUOxuZjs9XBTxavDjQyWeFwMD30UAJGakPhOOOj6ihXA19OvQ/tYuYTJ5C9QzeK90C/rYbg2fn+os3EGlb7iJZ2V3KGrNLdMcEtkiG5IiHicaNCn8OS/cI3d29iJE4ECaF711fyF8MG1H2tbkjULS3bsPNUvyvHfM2cjOPRhejayOh+CxQkc3wKar8ApvQCjiVRW05nO0ufHdPMcWJhUlchWYO5mOJTSO8vG/9YqpnTuDc2Gelc4gMK7KATdH3v1FsACPKNJdpt68IfZXgGYn5LtJ7zJB6Yw8Rewj1SaF/wFKXpYd+5JyK18wJTLVYSpiDzidh4DP+R6ZTAgMBAAE\u003d".decodeBase64()!!
        )
        val privateKey = RSAKey.Private(
            "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".decodeBase64()!!
        )
        RACertificateData(
            identifier = "identifier2",
            registrationToken = "registrationToken2",
            registeredAt = Instant.ofEpochMilli(12345),
            publicKeyRegisteredAt = Instant.ofEpochMilli(6789),
            rsaPublicKey = publicKey,
            rsaPrivateKey = privateKey,
            certificateReceivedAt = Instant.ofEpochMilli(123456789),
            encryptedDataEncryptionkey = "ZW5jcnlwdGVkRGF0YUVuY3J5cHRpb25rZXk=".decodeBase64()!!,
            testCertificateQrCode = personATest2CertQRCodeString,
            certificateSeenByUser = true
        )
    }

    val personATest2CertScannedStoredData = GenericTestCertificateData(
        identifier = "identifier2",
        registeredAt = Instant.ofEpochMilli(12345),
        testCertificateQrCode = personATest2CertQRCodeString
    )

    val personATest2CertContainer = TestCertificateContainer(
        data = personATest2CertStoredData,
        qrCodeExtractor = qrCodeExtractor,
    )

    val personATest3CertNokeyStoredData = run {
        RACertificateData(
            identifier = "identifier2",
            registrationToken = "registrationToken2",
            registeredAt = Instant.ofEpochMilli(12345),
        )
    }
    val personATest3CertNokeyContainer = TestCertificateContainer(
        data = personATest3CertNokeyStoredData,
        qrCodeExtractor = qrCodeExtractor,
    )
    val personATest4CertPendingStoredData = run {
        val publicKey = RSAKey.Public(
            "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAnrJ8PbmbOmEEmHB/8yg1bnkUT7jcF4Xfy2Me5imJgLVYQ0cL9UNP91cfFUrgMFV3fHOc0Uuay10TmrBLaEdzqDEZQH4Kj0uZ+hVCtzntVKFviuUoh08wxFlogtc5Sy0NhuTGyC1W/i2AX1SDvet2xMcc1fE44rITQEEAlG8+nfGpbppFHezUOxuZjs9XBTxavDjQyWeFwMD30UAJGakPhOOOj6ihXA19OvQ/tYuYTJ5C9QzeK90C/rYbg2fn+os3EGlb7iJZ2V3KGrNLdMcEtkiG5IiHicaNCn8OS/cI3d29iJE4ECaF711fyF8MG1H2tbkjULS3bsPNUvyvHfM2cjOPRhejayOh+CxQkc3wKar8ApvQCjiVRW05nO0ufHdPMcWJhUlchWYO5mOJTSO8vG/9YqpnTuDc2Gelc4gMK7KATdH3v1FsACPKNJdpt68IfZXgGYn5LtJ7zJB6Yw8Rewj1SaF/wFKXpYd+5JyK18wJTLVYSpiDzidh4DP+R6ZTAgMBAAE\u003d".decodeBase64()!!
        )
        val privateKey = RSAKey.Private(
            "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".decodeBase64()!!
        )
        RACertificateData(
            identifier = "identifier2",
            registrationToken = "registrationToken2",
            registeredAt = Instant.ofEpochMilli(12345),
            publicKeyRegisteredAt = Instant.ofEpochMilli(6789),
            rsaPublicKey = publicKey,
            rsaPrivateKey = privateKey,
        )
    }

    val personATest4CertPendingContainer = TestCertificateContainer(
        data = personATest4CertPendingStoredData,
        qrCodeExtractor = qrCodeExtractor,
    )
}
