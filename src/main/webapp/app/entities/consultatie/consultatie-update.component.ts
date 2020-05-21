import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IConsultatie, Consultatie } from 'app/shared/model/consultatie.model';
import { ConsultatieService } from './consultatie.service';
import { IStudentInfo } from 'app/shared/model/student-info.model';
import { StudentInfoService } from 'app/entities/student-info/student-info.service';
import { IProfesorInfo } from 'app/shared/model/profesor-info.model';
import { ProfesorInfoService } from 'app/entities/profesor-info/profesor-info.service';

type SelectableEntity = IStudentInfo | IProfesorInfo;

@Component({
  selector: 'jhi-consultatie-update',
  templateUrl: './consultatie-update.component.html'
})
export class ConsultatieUpdateComponent implements OnInit {
  isSaving = false;
  studentinfos: IStudentInfo[] = [];
  profesorinfos: IProfesorInfo[] = [];

  editForm = this.fb.group({
    id: [],
    data: [null, [Validators.required]],
    rezolvata: [],
    acceptata: [],
    student: [],
    profesor: []
  });

  constructor(
    protected consultatieService: ConsultatieService,
    protected studentInfoService: StudentInfoService,
    protected profesorInfoService: ProfesorInfoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consultatie }) => {
      if (!consultatie.id) {
        const today = moment().startOf('day');
        consultatie.data = today;
      }

      this.updateForm(consultatie);

      this.studentInfoService.query().subscribe((res: HttpResponse<IStudentInfo[]>) => (this.studentinfos = res.body || []));

      this.profesorInfoService.query().subscribe((res: HttpResponse<IProfesorInfo[]>) => (this.profesorinfos = res.body || []));
    });
  }

  updateForm(consultatie: IConsultatie): void {
    this.editForm.patchValue({
      id: consultatie.id,
      data: consultatie.data ? consultatie.data.format(DATE_TIME_FORMAT) : null,
      rezolvata: consultatie.rezolvata,
      acceptata: consultatie.acceptata,
      student: consultatie.student,
      profesor: consultatie.profesor
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consultatie = this.createFromForm();
    if (consultatie.student !== null) {
      consultatie.acceptata = true;
    }
    if (consultatie.id !== undefined) {
      this.subscribeToSaveResponse(this.consultatieService.update(consultatie));
    } else {
      this.subscribeToSaveResponse(this.consultatieService.create(consultatie));
    }
  }

  private createFromForm(): IConsultatie {
    return {
      ...new Consultatie(),
      id: this.editForm.get(['id'])!.value,
      data: this.editForm.get(['data'])!.value ? moment(this.editForm.get(['data'])!.value, DATE_TIME_FORMAT) : undefined,
      rezolvata: this.editForm.get(['rezolvata'])!.value,
      acceptata: this.editForm.get(['acceptata'])!.value,
      student: this.editForm.get(['student'])!.value,
      profesor: this.editForm.get(['profesor'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsultatie>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
