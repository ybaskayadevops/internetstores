package com.gmbh.internetstores.frameworks.ui.addbike.validator;

import android.widget.EditText;
import android.widget.RadioButton;

public class AddBikeValidator {


  public static String  addBikeValidator( EditText editTextCategory, EditText editTextLocation, EditText editTextName,
      EditText editTextDescription,   RadioButton radioButtonFrameSize,RadioButton radioButtonPriceRange,String previewFilePath)
  {
    String errorMessage="";

    if(editTextCategory.getText()==null || editTextCategory.getText().toString().isEmpty())
    {
      errorMessage="Category can not be empty";
    }
    else  if(editTextLocation.getText()==null || editTextLocation.getText().toString().isEmpty())
    {
      errorMessage="Location can not be empty";
    }
    else  if(editTextName.getText()==null || editTextName.getText().toString().isEmpty())
    {
      errorMessage="Name can not be empty";
    }
    else  if(editTextDescription.getText()==null || editTextDescription.getText().toString().isEmpty())
    {
      errorMessage="Description can not be empty";
    }
    else  if(editTextName.getText()==null || editTextName.getText().toString().isEmpty())
    {
      errorMessage="Name can not be empty";
    }
    else if(radioButtonFrameSize==null)
    {
      errorMessage="You must select a frame size";
    }
    else if(radioButtonPriceRange==null)
    {
      errorMessage="You must select a price range";
    }
    else if(previewFilePath==null)
    {
      errorMessage="You must select an image";
    }

    return errorMessage;
  }

}
