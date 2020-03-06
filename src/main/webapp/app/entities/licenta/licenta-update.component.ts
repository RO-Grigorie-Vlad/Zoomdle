import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ILicenta, Licenta } from 'app/shared/model/licenta.model';
import { LicentaService } from './licenta.service';
import { IStudentInfo } from 'app/shared/model/student-info.model';
import { StudentInfoService } from 'app/entities/student-info/student-info.service';
import { IProfesorInfo } from 'app/shared/model/profesor-info.model';
import { ProfesorInfoService } from 'app/entities/profesor-info/profesor-info.service';

type SelectableEntity = IStudentInfo | IProfesorInfo;

@Component({
  selector: 'jhi-licenta-update',
  templateUrl: './licenta-update.component.html'
})
export class LicentaUpdateComponent implements OnInit {
  isSaving = false;
  studentinfos: IStudentInfo[] = [];
  profesorinfos: IProfesorInfo[] = [];

  editForm = this.fb.group({
    id: [],
    denumire: [null, [Validators.required]],
    descriere: [],
    atribuita: [],
    studentInfo: [],
    profesor: []
  });

  constructor(
    protected licentaService: LicentaService,
    protected studentInfoService: StudentInfoService,
    protected profesorInfoService: ProfesorInfoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ licenta }) => {
      this.updateForm(licenta);

      this.studentInfoService
        .query({ filter: 'licenta-is-null' })
        .pipe(
          map((res: HttpResponse<IStudentInfo[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IStudentInfo[]) => {
          if (!licenta.studentInfo || !licenta.studentInfo.id) {
            this.studentinfos = resBody;
          } else {
            this.studentInfoService
              .find(licenta.studentInfo.id)
              .pipe(
                map((subRes: HttpResponse<IStudentInfo>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IStudentInfo[]) => (this.studentinfos = concatRes));
          }
        });

      this.profesorInfoService.query().subscribe((res: HttpResponse<IProfesorInfo[]>) => (this.profesorinfos = res.body || []));
    });
  }

  updateForm(licenta: ILicenta): void {
    this.editForm.patchValue({
      id: licenta.id,
      denumire: licenta.denumire,
      descriere: licenta.descriere,
      atribuita: licenta.atribuita,
      studentInfo: licenta.studentInfo,
      profesor: licenta.profesor
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const licenta = this.createFromForm();
    if (licenta.id !== undefined) {
      this.subscribeToSaveResponse(this.licentaService.update(licenta));
    } else {
      this.subscribeToSaveResponse(this.licentaService.create(licenta));
    }
  }

  private createFromForm(): ILicenta {
    return {
      ...new Licenta(),
      id: this.editForm.get(['id'])!.value,
      denumire: this.editForm.get(['denumire'])!.value,
      descriere: this.editForm.get(['descriere'])!.value,
      atribuita: this.editForm.get(['atribuita'])!.value,
      studentInfo: this.editForm.get(['studentInfo'])!.value,
      profesor: this.editForm.get(['profesor'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILicenta>>): void {
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
