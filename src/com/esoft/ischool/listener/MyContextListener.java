package  com.esoft.ischool.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.esoft.ischool.util.License;

public class MyContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Le contexte de l'application de Education vient d'�tre d�truit.");
	}

	@SuppressWarnings("unchecked")
	public void contextInitialized(ServletContextEvent sce) {

		System.out.println("Verification de la license...");

		try {
			/*
			 * String reportPath =
			 * sce.getServletContext().getRealPath("/license") + File.separator;
			 * if (!isLicenseValid(reportPath + "license.lic")) { System.out
			 * .println("Invalid License. Shuting down the system...");
			 * System.exit(0); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Le contexte de l'application de Education vient d'�tre cr��.");
		System.out.println("Voici les param�tres d'initialisation du contexte.");
		Enumeration<String> initParams = sce.getServletContext().getInitParameterNames();
		while (initParams.hasMoreElements()) {
			String name = initParams.nextElement();
			System.out.println(name + ":" + sce.getServletContext().getInitParameter(name));
		}
		// count connected
		Integer connected = new Integer(0);
		sce.getServletContext().setAttribute("connected", connected);
	}

	private boolean isLicenseValid(String licenseFile) {
		try {
			System.out.println("License File: " + licenseFile);
			// Security.addProvider(new BouncyCastleProvider());

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");

			// create the keys

			RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(
					new BigInteger(
							"b3f9a47ce4e363ab86922445caaf53395e6eba00d241f0b42fe225db0ae97c608c0747806737ff94e156db5d593d7281638f1b6aed5c428b8dc87ddf5297fa8dd9edf2c5ac9b7cdf658e6c756f77e05bd96d5783214153ecfbc9bb485e53b4e6a0058265e8a3a68469c22842a4e18bd994794904da1a7613ddd29c5bd9a13858308e51175e534166e68af3ea540bb72e98dfafc2c7e109e2cd966c0e1eb82e764c3986cdd27dd270a7a3bac8e7041be0106bf0428aae24b50a9465bce92d9f0c358101bcb36aaedeb057525e33cd9204a19b987036565db9ffb62459cb7e7c6bf77e35ad74cc681991a4fddbf49b6eda7d3ca932712288b392298b2c1d6d82d3",
							16),
					new BigInteger(
							"3d8e21cb31c1f0220769ce8c2c51a0e65b3d05d32816b38bfd609ccff9407870d113e049b383fc9f601f03f23e867cb67265ccdbda89169d8285d33f61916779c2d2d698f37b4ecf5d7dc3ecba8e46a7438b59461946adbcbd35771fbe5b64e108543a103eec5214b1d35d4fc5f2fa91156e12225db2753640453b8352c10872d4612ec3fa37e5dbd510bf09c869c9e873fd5055b2d05ed223646b7aa5da8f763770dbe6ffcad5a9dce5801e397640f63b4286441b3b5cad72af1679a8f864b867af80bebc4c8b39d9dd754c9f7c4d1a1fa5bbc09deef2a90a1a48b7ff0b8d87bc1d3077a2c809572450bf6e9f4033ba0c82d3a8e8a8709fb40788022f85fab9",
							16));

			RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);

			// decryption step

			cipher.init(Cipher.DECRYPT_MODE, privKey);

			FileInputStream fileIn = new FileInputStream(licenseFile);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			SealedObject ooo = (SealedObject) in.readObject();
			in.close();
			fileIn.close();

			License gg = (License) ooo.getObject(cipher);
			System.out.println(
					"LICENSE MAC ADDRESS:" + gg.getMacAddress() + "\nLICENSE EXPIRATION DATE: " + gg.getExpiration());

			if (isMACMatching(gg.getMacAddress())) {
				System.out.println("MAC is Matching");

				if (gg.getExpiration().before(new Date())) {
					System.out.println("LICENSE EXPIRED");
				} else {
					System.out.println("License is valid");
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	private boolean isMACMatching(String mac) {
		try {
			byte[] macAddress;
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets)) {
				macAddress = netint.getHardwareAddress();

				StringBuilder macc = new StringBuilder();
				if (macAddress != null) {
					for (byte b : macAddress) {
						macc.append(String.format("%1$02X", b));
					}
					System.out.println("-->" + macc);

					if (mac.equalsIgnoreCase(macc.toString()))
						return true;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
