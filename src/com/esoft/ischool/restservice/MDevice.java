package com.esoft.ischool.restservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;

import org.richfaces.component.html.HtmlDropDownMenu;
import org.richfaces.component.html.HtmlMenuItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.security.model.Menu;

@Component("mDevice")
@Scope("session")
public class MDevice extends BaseBean implements Serializable {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HtmlDropDownMenu menu;
	HtmlDropDownMenu dossiers;
	private List<HtmlDropDownMenu> dropDowns = new ArrayList<HtmlDropDownMenu>();
	
	
    public List<HtmlDropDownMenu> getDropDowns() {
		return dropDowns;
	}

	public void setDropDowns(List<HtmlDropDownMenu> dropDowns) {
		this.dropDowns = dropDowns;
	}

	public HtmlDropDownMenu getDossiers() {
		Set<Menu> userMenu = (Set<Menu>) getSessionParameter("menus");
		if (userMenu != null)
			for (Menu menu : userMenu) {

				if (menu.getMenuParent() == null) {
					HtmlDropDownMenu ddm = new HtmlDropDownMenu();
			        HtmlOutputLabel hol = new HtmlOutputLabel();
			        HtmlOutputText hot = new HtmlOutputText();
			        hot.setValue("Some text");
			        HtmlGraphicImage hgi = new HtmlGraphicImage();
			        hgi.setUrl("img/some_image.gif");
			        hgi.setStyle("vertical-align: bottom;");
			        hol.getChildren().add(hot);
			        hol.getChildren().add(hgi);
			        ddm.getFacets().put("label", hol);
			                   
			        HtmlMenuItem hmi = new HtmlMenuItem();
			        hmi.setValue("Some text");
			        ddm.getChildren().add(hmi);
			       
			        hmi = new HtmlMenuItem();
			        hmi.setValue("Another text");
			        ddm.getChildren().add(hmi);
			        dropDowns.add(ddm);
					if(menu.getId()==1){
						dossiers=ddm;
					}
				}

			}

		return dossiers;

	}

	public void setDossiers(HtmlDropDownMenu dossiers) {
		this.dossiers = dossiers;
	}

	public HtmlDropDownMenu getMenu() {

    menu = new HtmlDropDownMenu();
        HtmlOutputLabel hol = new HtmlOutputLabel();
        HtmlOutputText hot = new HtmlOutputText();
        hot.setValue("Some text");
        HtmlGraphicImage hgi = new HtmlGraphicImage();
        hgi.setUrl("img/some_image.gif");
        hgi.setStyle("vertical-align: bottom;");
        hol.getChildren().add(hot);
        hol.getChildren().add(hgi);
        menu.getFacets().put("label", hol);
                   
        HtmlMenuItem hmi = new HtmlMenuItem();
        hmi.setValue("Some text");
        menu.getChildren().add(hmi);
       
        hmi = new HtmlMenuItem();
        hmi.setValue("Another text");
        menu.getChildren().add(hmi);
       
       

        return menu;
    }

    public void setMenu(HtmlDropDownMenu menu) {
        this.menu = menu;
    }

}