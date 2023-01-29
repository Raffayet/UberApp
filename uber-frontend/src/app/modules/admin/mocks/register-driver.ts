import { FormGroup, FormControl, Validators } from "@angular/forms";
import { identityRevealedValidator } from "../../auth/pages/registration-page/registration-page.component";

const vehicleTypes = ["Standard", "Baby Seat", "Pet Friendly", "Baby Seat and Pet Friendly"];
 
const secondFormGroup = new  FormGroup({
    model:new FormControl('',[Validators.required]),
    vehicleType:new FormControl('',[Validators.required]),
  });

export {vehicleTypes, secondFormGroup};