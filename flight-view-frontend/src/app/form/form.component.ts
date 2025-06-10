import { Component, OnInit } from '@angular/core';
import {FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';
import {FormService} from "../../services/form.service";

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent {
  form: FormGroup;

  constructor(private builder: FormBuilder,
              private service: FormService) {
    this.form = this.builder.group({
      flightNumber: [''],
      flightDate: [''],
      company: [''],
      rating: [0],
      remarks: ['']
    });
  }

  changeRating(rating: number) {
    this.form.patchValue({ rating });
  }

  onSubmit() {
    if (this.form.valid) {
      this.service.postRating(this.form.value).subscribe({
        next: (res) => {
          this.form.reset();
        },
        error: (err) => {
          alert('Failure occured');
        }
      });
    }
  }
}
