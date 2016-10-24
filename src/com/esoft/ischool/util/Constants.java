package com.esoft.ischool.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Constants {
	

	int RESERVE = 1;
	int DISPONIBLE = 0;
	Integer SERIE_CLOTUREE = 0;
	Long ADMIN_ROLE_ID = 2L;
	Integer STUDENT_ROLE_CODE=3;
	Integer TEACHER_ROLE_CODE=4;
	Integer MAX_FILE_SIZE=1048576;
	String ADMIN_FIRST_NAME = "ADMIN";
	String DEFAULT_PAGE_SKIN = "classic";
	Short ACTIVE_STATUS = 1;
	Short INACTIVE_STUDENT=0;
	Short TEMP_STUDENT=2;
	Short TEMP_REJECTED_STUDENT=3;
	Short TEMP_ACCEPTED_STUDENT=4;
	
	String TEXT_ACTIVE_STATUS = "DOSSIER ACTIF";
	String TEXT_INACTIVE_STUDENT="DOSSIER INACTIF";
	String TEXT_TEMP_STUDENT="DOSSIER EN COURS D'EDTUDE";
	String TEXT_TEMP_REJECTED_STUDENT="DOSSIER REJETE";
	String TEXT_TEMP_ACCEPTED_STUDENT="DOSSIER ACCEPTE";
	
	
	String SCHOOL_CREATED = "Votre Etablissement est maintenant sur la plateforme iSchool";
	String REGISTRATION_RECEIVED = "Nous avons bien recu votre demande d'inscription";
	String SCHOOL_CREATED_NO_MAIL="L'Etablissement est cree avec succes mais l'envois" +
			" automatique d'E-mail au client a echoue. Veuillez le faire manuellement";
	String SCHOOL_CREATED_WITH_MAIL="L'Etablissement est cree avec succes. Un E-mail a ete envoye au client";
	String SCHOOL_DELETE_UNSUCCESSFULL="Vous ne pouvez pas supprimer cet Etablissement parce que les donnees qui y sont associees seront perdues. " +
			"\n Veuillez supprimer manuellement toutes les donnees avant de supprimer l'Etablissment.";
	
	String AVERAGE_CALCULATION_FAILED="Le calcul des moyennes a echoue.";
	String AVERAGE_CALCULATION_SUCCESSFUL="Le calcul des moyennes effectue avec succes.";
	String RESULTS_PUBLISHED_SUCCESSFULLY="Les resultats sont publie avec succes";
	String RESULTS_ALREADY_PUBLISHED="Les resultats sont deja publies";
	String RESULTS_LOCKED="Les resultats ont ete verrrouilles.";
	String RESULTS_UNLOCKED="Les resultats ont ete deverrrouilles.";
	String ALL_FIELDS_REQUIRED="Tous les champs sont obligatoires";
	String NO_RESULT_FOUND="Pas de resultat Trouve";
	
	String ALERT="Alert";
	String MAIL="Mail";
	String UNAPPROVED_MARK="Notes a approver";
	String UNAPPROVED_DEMAND="Demande non approve";
	String EXAM_TYPE="Type d'examen";
	String DESCRIPTION="Description";
	String CLASS="Classe";
	String COURSE="Cours";
	String PRODUCT="Produit";
	String QUANTITY="Quantite";
	String COMMENT="Commentaire";

	String SUBJECT_NOT_TEACHED_IN_CLASS = "Cette matiere n'est pas enseignee dans cette classe";
	String NOT_A_VALID_YEAR_OR_CLASS = "S'il vous plait selectionner une annee et une classe valide.";
	String MATRICULE_EXISTS="Ce matricule est deja pris";
	String MAX_SIZE_EXCEEDED="La taille du fichier doit etre inferieure a 1MB";
	static final Map<String, String> urls = new HashMap<String , String>() {{ 
	    put("identification", "/student.faces"); 
	    put("pidentification", "/teacher.faces"); 
	    put("adminSubject", "/subject.faces"); 
	    put("adminLevelClass", "/levelClass.faces");
	    put("adminSession", "/session.faces");
	    put("adminUser", "/user.faces");
	    put("adminExamType", "/examType.faces");
	    put("adminSchoolType", "/schoolType.faces");
	    put("adminSchoolLevel", "/schoolLevel.faces");
	    put("adminSchoolReligion", "/schoolReligion.faces");
	    put("adminLevel", "/level.faces");
	    put("adminPosition", "/position.faces");
	    put("adminCountry", "/country.faces");
	    put("adminCity", "/city.faces");
	    put("adminSchool", "/school.faces");
	    put("adminCourse", "/course.faces");
	    put("correspondance", "/correspondence.faces");
	    put("report", "/report.faces");
	}};
	
	static final Map<String, String> reportParameters = new HashMap<String , String>() {{ 
	    put("SUBREPORT_DIR", "/student.faces"); 
	}};
	
	public static Map<String, String> configurationMap = null;
	public static String CANNOT_CHANGE_STUDENT_STATUS="Vous ne pouvez pas liberer l'etudiant parce qu'il n'est pas inscrit dans votre etablissement";
	public static String CANNOT_CHANGE_TEACHER_STATUS="Vous ne pouvez pas liberer l'enseignant parce qu'il n'est pas inscrit dans votre etablissement";

	public static String SCHOOL_NAME = "\\$SCHOOL_NAME\\$";
	public static String TODAY_DATE = "\\$TODAY_DATE\\$";
	public static String SCHOOL_WEBSITE = "\\$SCHOOL_WEBSITE\\$";
	public static String SUBJECT_NAME = "\\$SUBJECT_NAME\\$";
	
	public static String TOTAL_TUITION = "\\$TOTAL_TUITION\\$";
	public static String PAYED_TUITION = "\\$PAYED_TUITION\\$";
	public static String DUE_TUITION = "\\$DUE_TUITION\\$";
	public static String STUDENT_NAME = "\\$STUDENT_NAME\\$";
	
	public static String PRODUCT_NAME = "\\$PRODUCT_NAME\\$";
	public static String MINIMUM_QUANTITY = "\\$MINIMUM_QUANTITY\\$";
	public static String QUANTITY_IN_STOCK = "\\$QUANTITY_IN_STOCK\\$";

	public static String REQUESTED_DATE = "\\$REQUESTED_DATE\\$";
	public static String PICKED_UP_DATE = "\\$PICKED_UP_DATE\\$";
	public static String QUANTITY_TO_BE_RETURNED = "\\$QUANTITY_TO_BE_RETURNED\\$";
	public static String QUANTITY_RETURNED = "\\$QUANTITY_RETURNED\\$";
	public static String DUE_DATE = "\\$DUE_DATE\\$";
	public static List<Integer> TO_BE_APPROVE = Arrays.asList(new Integer[]{ProductConsumerStatus.PENDING_APPROVAL.getValue()}); 
	public static List<String> TO_SHOW_NUMBER_OF_DAYS_BEFORE_DUE_DATE = Arrays.asList(new String[]{AlertType.TUITION_PAYMENT_OVER_DUE.getValue(), AlertType.PRODUCT_CONSUMER_OVER_DUE.getValue()});
}
