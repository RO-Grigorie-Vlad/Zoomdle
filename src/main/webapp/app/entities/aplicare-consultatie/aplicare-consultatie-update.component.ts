import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAplicareConsultatie, AplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';
import { AplicareConsultatieService } from './aplicare-consultatie.service';
import { IStudentInfo } from 'app/shared/model/student-info.model';
import { StudentInfoService } from 'app/entities/student-info/student-info.service';
import { IConsultatie } from 'app/shared/model/consultatie.model';
import { ConsultatieService } from 'app/entities/consultatie/consultatie.service';

type SelectableEntity = IStudentInfo | IConsultatie;

@Component({
  selector: 'jhi-aplicare-consultatie-update',
  templateUrl: './aplicare-consultatie-update.component.html'
})
export class AplicareConsultatieUpdateComponent implements OnInit {
  isSaving = false;
  studentinfos: IStudentInfo[] = [];
  consultaties: IConsultatie[] = [];

  editForm = this.fb.group({
    id: [],
    rezolvata: [],
    acceptata: [],
    student: [],
    consultatie: []
  });

  constructor(
    protected aplicareConsultatieService: AplicareConsultatieService,
    protected studentInfoService: StudentInfoService,
    protected consultatieService: ConsultatieService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aplicareConsultatie }) => {
      this.updateForm(aplicareConsultatie);

      this.studentInfoService.query().subscribe((res: HttpResponse<IStudentInfo[]>) => (this.studentinfos = res.body || []));

      this.consultatieService.query().subscribe((res: HttpResponse<IConsultatie[]>) => (this.consultaties = res.body || []));
    });
  }

  updateForm(aplicareConsultatie: IAplicareConsultatie): void {
    this.editForm.patchValue({
      id: aplicareConsultatie.id,
      rezolvata: aplicareConsultatie.rezolvata,
      acceptata: aplicareConsultatie.acceptata,
      student: aplicareConsultatie.student,
      consultatie: aplicareConsultatie.consultatie
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aplicareConsultatie = this.createFromForm();
    if (aplicareConsultatie.id !== undefined) {
      this.subscribeToSaveResponse(this.aplicareConsultatieService.update(aplicareConsultatie));
    } else {
      this.subscribeToSaveResponse(this.aplicareConsultatieService.create(aplicareConsultatie));
    }
  }

  private createFromForm(): IAplicareConsultatie {
    return {
      ...new AplicareConsultatie(),
      id: this.editForm.get(['id'])!.value,
      rezolvata: this.editForm.get(['rezolvata'])!.value,
      acceptata: this.editForm.get(['acceptata'])!.value,
      student: this.editForm.get(['student'])!.value,
      consultatie: this.editForm.get(['consultatie'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAplicareConsultatie>>): void {
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
