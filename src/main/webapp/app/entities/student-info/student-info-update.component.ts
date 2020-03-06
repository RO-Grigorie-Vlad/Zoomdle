import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStudentInfo, StudentInfo } from 'app/shared/model/student-info.model';
import { StudentInfoService } from './student-info.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IProfesorInfo } from 'app/shared/model/profesor-info.model';
import { ProfesorInfoService } from 'app/entities/profesor-info/profesor-info.service';

type SelectableEntity = IUser | IProfesorInfo;

@Component({
  selector: 'jhi-student-info-update',
  templateUrl: './student-info-update.component.html'
})
export class StudentInfoUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  profesorinfos: IProfesorInfo[] = [];

  editForm = this.fb.group({
    id: [],
    user: [null, Validators.required],
    profesor: []
  });

  constructor(
    protected studentInfoService: StudentInfoService,
    protected userService: UserService,
    protected profesorInfoService: ProfesorInfoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentInfo }) => {
      this.updateForm(studentInfo);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.profesorInfoService.query().subscribe((res: HttpResponse<IProfesorInfo[]>) => (this.profesorinfos = res.body || []));
    });
  }

  updateForm(studentInfo: IStudentInfo): void {
    this.editForm.patchValue({
      id: studentInfo.id,
      user: studentInfo.user,
      profesor: studentInfo.profesor
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentInfo = this.createFromForm();
    if (studentInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.studentInfoService.update(studentInfo));
    } else {
      this.subscribeToSaveResponse(this.studentInfoService.create(studentInfo));
    }
  }

  private createFromForm(): IStudentInfo {
    return {
      ...new StudentInfo(),
      id: this.editForm.get(['id'])!.value,
      user: this.editForm.get(['user'])!.value,
      profesor: this.editForm.get(['profesor'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentInfo>>): void {
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
