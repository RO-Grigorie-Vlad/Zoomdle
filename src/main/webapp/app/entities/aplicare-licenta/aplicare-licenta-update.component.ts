import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAplicareLicenta, AplicareLicenta } from 'app/shared/model/aplicare-licenta.model';
import { AplicareLicentaService } from './aplicare-licenta.service';
import { IStudentInfo } from 'app/shared/model/student-info.model';
import { StudentInfoService } from 'app/entities/student-info/student-info.service';
import { ILicenta } from 'app/shared/model/licenta.model';
import { LicentaService } from 'app/entities/licenta/licenta.service';

type SelectableEntity = IStudentInfo | ILicenta;

@Component({
  selector: 'jhi-aplicare-licenta-update',
  templateUrl: './aplicare-licenta-update.component.html'
})
export class AplicareLicentaUpdateComponent implements OnInit {
  isSaving = false;
  studentinfos: IStudentInfo[] = [];
  licentas: ILicenta[] = [];

  editForm = this.fb.group({
    id: [],
    rezolvata: [],
    acceptata: [],
    student: [],
    licenta: []
  });

  constructor(
    protected aplicareLicentaService: AplicareLicentaService,
    protected studentInfoService: StudentInfoService,
    protected licentaService: LicentaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aplicareLicenta }) => {
      this.updateForm(aplicareLicenta);

      this.studentInfoService.query().subscribe((res: HttpResponse<IStudentInfo[]>) => (this.studentinfos = res.body || []));

      this.licentaService.query().subscribe((res: HttpResponse<ILicenta[]>) => (this.licentas = res.body || []));
    });
  }

  updateForm(aplicareLicenta: IAplicareLicenta): void {
    this.editForm.patchValue({
      id: aplicareLicenta.id,
      rezolvata: aplicareLicenta.rezolvata,
      acceptata: aplicareLicenta.acceptata,
      student: aplicareLicenta.student,
      licenta: aplicareLicenta.licenta
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aplicareLicenta = this.createFromForm();
    if (aplicareLicenta.id !== undefined) {
      this.subscribeToSaveResponse(this.aplicareLicentaService.update(aplicareLicenta));
    } else {
      this.subscribeToSaveResponse(this.aplicareLicentaService.create(aplicareLicenta));
    }
  }

  private createFromForm(): IAplicareLicenta {
    return {
      ...new AplicareLicenta(),
      id: this.editForm.get(['id'])!.value,
      rezolvata: this.editForm.get(['rezolvata'])!.value,
      acceptata: this.editForm.get(['acceptata'])!.value,
      student: this.editForm.get(['student'])!.value,
      licenta: this.editForm.get(['licenta'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAplicareLicenta>>): void {
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
