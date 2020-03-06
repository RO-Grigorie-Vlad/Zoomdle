import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudentInfo } from 'app/shared/model/student-info.model';

@Component({
  selector: 'jhi-student-info-detail',
  templateUrl: './student-info-detail.component.html'
})
export class StudentInfoDetailComponent implements OnInit {
  studentInfo: IStudentInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentInfo }) => (this.studentInfo = studentInfo));
  }

  previousState(): void {
    window.history.back();
  }
}
