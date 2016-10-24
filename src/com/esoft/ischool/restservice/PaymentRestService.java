// #Create Payment Using PayPal Sample
// This sample code demonstrates how you can process a 
// PayPal Account based Payment.
// API used: /v1/payments/payment
package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.esoft.ischool.model.Mail;
import com.esoft.ischool.model.PaymentHistory;
import com.esoft.ischool.model.PaymentType;
import com.esoft.ischool.model.Project;
import com.esoft.ischool.model.Transaction;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.EventService;
import com.esoft.ischool.service.PaymentService;
import com.esoft.ischool.util.GenerateAccessToken;
import com.esoft.ischool.util.ResultPrinter;
import com.esoft.ischool.util.SimpleMail;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

/**
 * @author Panawe Batanado
 * 
 */

@RestController
@RequestMapping("/service/payment")

public class PaymentRestService {
	@Autowired
	PaymentService paymentService;
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(PaymentRestService.class);
	Map<String, String> map = new HashMap<String, String>();

	public void init() {
		// ##Load Configuration
		// Load SDK configuration for
		// the resource. This intialization code can be
		// done as Init Servlet.
		InputStream is = PaymentRestService.class.getResourceAsStream("/sdk_config.properties");
		try {
			PayPalResource.initConfig(is);
		} catch (PayPalRESTException e) {
			LOGGER.fatal(e.getMessage());
		}

	}

	public static void main(String args[]) {
		PaymentRestService pp = new PaymentRestService();
		try {
			pp.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/createPayment", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String createPayment(@RequestBody Payment payment) throws PayPalRESTException {
		Payment createdPayment = null;
		APIContext apiContext = null;
		String accessToken = null;
		InputStream is = PaymentRestService.class.getResourceAsStream("/sdk_config.properties");
		try {
			PayPalResource.initConfig(is);
		} catch (PayPalRESTException e) {
			LOGGER.fatal(e.getMessage());
			throw e;
		}

		try {
			accessToken = GenerateAccessToken.getAccessToken();
			apiContext = new APIContext(accessToken);
		} catch (PayPalRESTException e) {
			LOGGER.error(e);
			e.printStackTrace();
			throw e;
		}

		try {

			// ###Payer
			// A resource representing a Payer that funds a payment
			// Payment Method
			// as 'paypal'
			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");
			payment.setPayer(payer);
			try {
				String desc = payment.getTransactions().get(0).getDescription();
				if (desc != null) {
					Project proj = (Project) paymentService.getById(Project.class,  (long) Integer.parseInt(desc));
					payment.getTransactions().get(0).setDescription(proj.getTitle());
				}
			} catch (Exception e) {
				e.printStackTrace();
				payment.getTransactions().get(0).setDescription("Don Pour ARELBOU");
			}
			// ###Redirect URLs
			RedirectUrls redirectUrls = new RedirectUrls();
			redirectUrls.setCancelUrl("http://www.arelbou.com/#/pages/cancelDonate");
			redirectUrls.setReturnUrl("http://www.arelbou.com/#/pages/donate");
			payment.setRedirectUrls(redirectUrls);

			createdPayment = payment.create(apiContext);
			LOGGER.info("Created payment with id = " + createdPayment.getId() + " and status = "
					+ createdPayment.getState());
			// ###Payment Approval Url
			Iterator<Links> links = createdPayment.getLinks().iterator();
			while (links.hasNext()) {
				Links link = links.next();
				if (link.getRel().equalsIgnoreCase("approval_url")) {
					// req.setAttribute("redirectURL", link.getHref());
					LOGGER.info("Redirect URL" + link.getHref());
					return link.getHref();
				}
			}
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/submitFee", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String submitFee(@RequestBody Payment payment) throws PayPalRESTException {
		Payment createdPayment = null;
		APIContext apiContext = null;
		String accessToken = null;
		InputStream is = PaymentRestService.class.getResourceAsStream("/sdk_config.properties");
		try {
			PayPalResource.initConfig(is);
		} catch (PayPalRESTException e) {
			LOGGER.fatal(e.getMessage());
			throw e;
		}

		try {
			accessToken = GenerateAccessToken.getAccessToken();
			apiContext = new APIContext(accessToken);
		} catch (PayPalRESTException e) {
			LOGGER.error(e);
			e.printStackTrace();
			throw e;
		}

		try {

			// ###Payer
			// A resource representing a Payer that funds a payment
			// Payment Method
			// as 'paypal'
			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");
			payment.setPayer(payer);

			// ###Redirect URLs
			RedirectUrls redirectUrls = new RedirectUrls();
			redirectUrls.setCancelUrl("http://www.arelbou.com/#/pages/cancelDonate");
			redirectUrls.setReturnUrl("http://www.arelbou.com/#/pages/fees");
			payment.setRedirectUrls(redirectUrls);

			createdPayment = payment.create(apiContext);
			LOGGER.info("Created payment with id = " + createdPayment.getId() + " and status = "
					+ createdPayment.getState());
			// ###Payment Approval Url
			Iterator<Links> links = createdPayment.getLinks().iterator();
			while (links.hasNext()) {
				Links link = links.next();
				if (link.getRel().equalsIgnoreCase("approval_url")) {
					// req.setAttribute("redirectURL", link.getHref());
					LOGGER.info("Redirect URL" + link.getHref());
					return link.getHref();
				}
			}
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/getPay", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody PaymentHistory getPay(@RequestBody Object data) {
		String paymentId = null;
		String token = null;
		String payerId = null;
		Long userId = null;
		try {
			String theData[] = data.toString().replaceAll("\\{", "").replaceAll("\\}", "").split(",");
			for (String a : theData) {
				String b[] = a.split("=");
				if (b[0].trim().equals("paymentId")) {
					paymentId = b[1];
				} else if (b[0].trim().equals("token")) {
					token = b[1];
				} else if (b[0].trim().equals("PayerID")) {
					payerId = b[1];
				} else if (b[0].trim().equals("userId")) {
					userId = b[1] == null ? null : new Long(b[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		PaymentHistory ph = null;
		try {
			ph = (PaymentHistory) paymentService.findByColumn(PaymentHistory.class, "paymentId", paymentId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ph;
	}

	@RequestMapping(value = "/makePayment", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody PaymentHistory makePayment(@RequestBody Object data) throws PayPalRESTException {
		String paymentId = null;
		String token = null;
		String payerId = null;
		Long userId = null;
		System.out.println("Enter public @ResponseBody PaymentHistory makePayment(@RequestBody Object data) throws PayPalRESTException");
		try {
			String theData[] = data.toString().replaceAll("\\{", "").replaceAll("\\}", "").split(",");
			for (String a : theData) {
				String b[] = a.split("=");
				if (b[0].trim().equals("paymentId")) {
					paymentId = b[1];
				} else if (b[0].trim().equals("token")) {
					token = b[1];
				} else if (b[0].trim().equals("PayerID")) {
					payerId = b[1];
				} else if (b[0].trim().equals("userId")) {
					userId = b[1] == null ? null : new Long(b[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		APIContext apiContext = null;
		String accessToken = null;
		InputStream is = PaymentRestService.class.getResourceAsStream("/sdk_config.properties");
		try {
			PayPalResource.initConfig(is);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			LOGGER.fatal(e.getMessage());
			throw e;
		}

		try {
			accessToken = GenerateAccessToken.getAccessToken();
			apiContext = new APIContext(accessToken);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			LOGGER.error(e);
			e.printStackTrace();
			throw e;
		}
		System.out.println("paymentId="+paymentId);
		PaymentHistory ph = null;
		try {
			ph = (PaymentHistory) paymentService.findByColumn(PaymentHistory.class, "paymentId", paymentId);
			System.out.println("Payment History="+ph);
			if (ph == null) {
				
				Payment payment = new Payment();
				payment.setId(paymentId);
				PaymentExecution paymentExecute = new PaymentExecution();
				paymentExecute.setPayerId(payerId);
				System.out.println("Before Executing Payment paymentId="+paymentId);
				Payment resultPayment = payment.execute(apiContext, paymentExecute);
				System.out.println("After Executing Payment paymentId="+paymentId);
				ph = new PaymentHistory(resultPayment);
				if (userId != null && userId > 0) {
					User user = (User) paymentService.getById(User.class, userId);
					ph.setUser(user);
				}
				// 3 == Don.
				PaymentType pType = (PaymentType) paymentService.getById(PaymentType.class, 3L);
				ph.setPaymentType(pType);
				ph.setPaymentId(paymentId);
				try {
					String desc = ph.getDescription();
					if (desc != null) {
						Project project = (Project) paymentService.findByColumn(Project.class, "title", desc); 
						ph.setProject(project);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Before Saving Payment History:"+ph);
				paymentService.save(ph);
				System.out.println("After Saving Payment History");
				Transaction trans = new Transaction(ph);
				System.out.println("Before Saving Transaction ="+trans);
				paymentService.save(trans);
				System.out.println("After Saving Transaction ");
				LOGGER.debug(trans);
				LOGGER.debug(ph);
				LOGGER.debug(resultPayment);
				try {
					System.out.println("Before sending e-mail to: "+ph.getEmail());
					// send e-mail
					String mail = "<div class=\"row\">                                                 "
							+ "		<div class=\"col-xs-12\">                                                        "
							+ "			<i class=\"fa fa-thumbs-up fa-5x text-green-300\"> Merci pour                "
							+ "				votre don!</i>                                                           "
							+ "			<hr />                                                                       "
							+ "			<div class=\"invoice-title\">                                                "
							+ "				<h2>Recu</h2>                                                            "
							+ "				<h3 class=\"pull-right\">Don #" + ph.getPaymentId()
							+ "</h3>                    " 
							+ "				<h3 class=\"pull-right\"><strong>Montant donne $"
							+ ph.getAmount() + "</strong></h3>                    "
									+ "				<h3 class=\"pull-right\"><strong>Frais de transfer: $"
									+ ph.getFee() + "</strong></h3>                    "
											+ "				<h3 class=\"pull-right\"><strong>Montant transmis a ARELBOU: $"
											+ (ph.getAmount()-ph.getFee()) + "</strong></h3>                    "

							+ "			</div>                                                                       "
							+ "			<hr>                                                                         "
							+ "			<div class=\"row\">                                                          "
							+ "				<div class=\"col-xs-6\">                                                 "
							+ "					<address>                                                            "
							+ "						<strong>Donateur:</strong><br>" + ph.getFirstName() + " "
							+ ph.getLastName()
							+ (ph.getBaLine1() != null ? " <br>" + ph.getBaLine1() + "<br>" + ph.getBaLine2() + "<br>"
									+ ph.getBaCity() + ", " + ph.getBaState() + " " + ph.getBaPostalCode() + "<br>"
									+ ph.getBaCountryCode() : "")
							+ "					</address>                                                           "
							+ "				</div> <br>                                                                  "
							+ "				<div class=\"col-xs-6 text-right\">                                      "
							+ "					<address>                                                            "
							+ "						<strong>En faveur de:</strong><br> Association des               "
							+ "						Resortissants de LAMA BOU. <br>      "
							+ "						Lome - Togo                            "
							+ "					</address>                                                           "
							+ "				</div>                                                                   "
							+ "			</div>    <br>                                                                   "
							+ "			<div class=\"row\">                                                          "
							+ "				<div class=\"col-xs-6\">                                                 "
							+ "					<address>                                                            "
							+ "						<strong>Mode de payment:</strong><br>                            "
							+ "						" + ph.getPaymentMethod() + "<br>" + ph.getEmail()
							+ "					</address>                                                           "
							+ "				</div>  <br>                                                                 "
							+ "				<div class=\"col-xs-6 text-right\">                                      "
							+ "					<address>                                                            "
							+ "						<strong>Date de don:</strong><br> " + ph.getCreateDate()
							+ "<br>     "
							+ "						<br>                                                             "
							+ "					</address>                                                           "
							+ "				</div>                                                                   "
							+ "			</div>                                                                       "
							+ "		</div>                                                                           "
							+ "	</div>                                                                               ";

					SimpleMail.sendMail("Merci pour votre Don de $" + ph.getAmount() + " a ARELBOU", mail,
							"arelboutg@gmail.com", ph.getEmail(), "smtp.gmail.com", "arelboutg@gmail.com",
							"arelboutg123");
					System.out.println("After sending e-mail to: "+ph.getEmail());
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.error(e);
				}
			} else {
				return ph;
			}

		} catch (PayPalRESTException e) {
			e.printStackTrace();
			if (e.getMessage().contains("PAYMENT_ALREADY_DONE")) {
				Map<String, String> containerMap = new HashMap<String, String>();
				containerMap.put("count", "10");
				com.paypal.api.payments.PaymentHistory phs = Payment.list(accessToken, containerMap);
			} else {
				throw e;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Exit public @ResponseBody PaymentHistory makePayment(@RequestBody Object data) throws PayPalRESTException");

		return ph;
	}

	@RequestMapping(value = "/payFee", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody PaymentHistory payFee(@RequestBody Object data) throws PayPalRESTException {
		String paymentId = null;
		String token = null;
		String payerId = null;
		Long userId = null;
		
		try {
			String theData[] = data.toString().replaceAll("\\{", "").replaceAll("\\}", "").split(",");
			for (String a : theData) {
				String b[] = a.split("=");
				if (b[0].trim().equals("paymentId")) {
					paymentId = b[1];
				} else if (b[0].trim().equals("token")) {
					token = b[1];
				} else if (b[0].trim().equals("PayerID")) {
					payerId = b[1];
				} else if (b[0].trim().equals("userId")) {
					userId = b[1] == null ? null : new Long(b[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		APIContext apiContext = null;
		String accessToken = null;
		InputStream is = PaymentRestService.class.getResourceAsStream("/sdk_config.properties");
		try {
			PayPalResource.initConfig(is);
		} catch (PayPalRESTException e) {
			LOGGER.fatal(e.getMessage());
			throw e;
		}

		try {
			accessToken = GenerateAccessToken.getAccessToken();
			apiContext = new APIContext(accessToken);
		} catch (PayPalRESTException e) {
			LOGGER.error(e);
			e.printStackTrace();
			throw e;
		}
		PaymentHistory ph = null;
		Payment resultPayment = null;
		try {
			ph = (PaymentHistory) paymentService.findByColumn(PaymentHistory.class, "paymentId", paymentId);
			if (ph == null) {
				Payment payment = new Payment();
				payment.setId(paymentId);
				PaymentExecution paymentExecute = new PaymentExecution();
				paymentExecute.setPayerId(payerId);
				resultPayment = payment.execute(apiContext, paymentExecute);
				ph = new PaymentHistory(resultPayment);
				if (userId != null && userId > 0) {
					User user = (User) paymentService.getById(User.class, userId);
					Calendar date = new GregorianCalendar();
					// reset hour, minutes, seconds and millis
					date.set(Calendar.HOUR_OF_DAY, 0);
					date.set(Calendar.MINUTE, 0);
					date.set(Calendar.SECOND, 0);
					date.set(Calendar.MILLISECOND, 0);
					// next day
					date.add(Calendar.YEAR, 1);
					user.setMembershipRenewDate(date.getTime());
					user.setStatus((short) 1);
					paymentService.save(user);
					ph.setUser(user);
				}
				// 3 == Frais Annuels.
				PaymentType pType = (PaymentType) paymentService.getById(PaymentType.class, 2L);
				ph.setPaymentType(pType);
				ph.setPaymentId(paymentId);
				paymentService.save(ph);
				Transaction trans = new Transaction(ph);
				paymentService.save(trans);
				LOGGER.debug(trans);
				LOGGER.debug(ph);
				LOGGER.debug(resultPayment);
				try {
					// send e-mail
					String mail = "<div class=\"row\">                                                 "
							+ "		<div class=\"col-xs-12\">                                                        "
							+ "			<i class=\"fa fa-thumbs-up fa-5x text-green-300\"> Merci d'avoir paye vos    "
							+ "				frais de membre annuel.</i>                                              "
							+ "			<hr />                                                                       "
							+ "			<div class=\"invoice-title\">                                                "
							+ "				<h2>Recu</h2>                                                            "
							+ "				<h3 class=\"pull-right\">Payement #" + ph.getPaymentId() + "</h3>          "
							+ "				<h3 class=\"pull-right\"><strong>Montant donne $"
							+ ph.getAmount() + "</strong></h3>                    "
									+ "				<h3 class=\"pull-right\"><strong>Frais de transfer: $"
									+ ph.getFee() + "</strong></h3>                    "
											+ "				<h3 class=\"pull-right\"><strong>Montant transmis a ARELBOU: $"
											+ (ph.getAmount()-ph.getFee()) + "</strong></h3>                    "

							+ "			</div>                                                                       "
							+ "			<hr>                                                                         "
							+ "			<div class=\"row\">                                                          "
							+ "				<div class=\"col-xs-6\">                                                 "
							+ "					<address>                                                            "
							+ "						<strong>Donateur:</strong><br>" + ph.getFirstName() + " "
							+ ph.getLastName()
							+ (ph.getBaLine1() != null ? " <br>" + ph.getBaLine1() + "<br>" + ph.getBaLine2() + "<br>"
									+ ph.getBaCity() + ", " + ph.getBaState() + " " + ph.getBaPostalCode() + "<br>"
									+ ph.getBaCountryCode() : "")
							+ "					</address>                                                           "
							+ "				</div> <br>                                                              "
							+ "				<div class=\"col-xs-6 text-right\">                                      "
							+ "					<address>                                                            "
							+ "						<strong>En faveur de:</strong><br> Association des               "
							+ "						Resortissants de LAMA BOU. <br>      "
							+ "						Lome - Togo                            "
							+ "					</address>                                                           "
							+ "				</div>                                                                   "
							+ "			</div>    <br>                                                               "
							+ "			<div class=\"row\">                                                          "
							+ "				<div class=\"col-xs-6\">                                                 "
							+ "					<address>                                                            "
							+ "						<strong>Mode de payment:</strong><br>                            "
							+ "						" + ph.getPaymentMethod() + "<br>" + ph.getEmail()
							+ "					</address>                                                           "
							+ "				</div>  <br>                                                             "
							+ "				<div class=\"col-xs-6 text-right\">                                      "
							+ "					<address>                                                            "
							+ "						<strong>Date de payement:</strong><br> " + ph.getCreateDate()
							+ "<br>"
							+ "						<br>                                                             "
							+ "					</address>                                                           "
							+ "				</div>                                                                   "
							+ "			</div>                                                                       "
							+ "		</div>                                                                           "
							+ "	</div>                                                                               ";

					SimpleMail.sendMail(
							"Merci d'avoir paye vos frais de membre annuel de " + ph.getAmount() + " a ARELBOU", mail,
							"arelboutg@gmail.com", ph.getEmail(), "smtp.gmail.com", "arelboutg@gmail.com",
							"arelboutg123");

				} catch (Exception e) {
					LOGGER.error(e);
				}
			} else {
				return ph;
			}

		} catch (PayPalRESTException e) {
			e.printStackTrace();
			if (e.getMessage().contains("PAYMENT_ALREADY_DONE")) {

			} else {
				throw e;
			}
		}

		return ph;
	}

}
