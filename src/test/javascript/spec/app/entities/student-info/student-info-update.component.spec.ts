import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { StudentInfoUpdateComponent } from 'app/entities/student-info/student-info-update.component';
import { StudentInfoService } from 'app/entities/student-info/student-info.service';
import { StudentInfo } from 'app/shared/model/student-info.model';

describe('Component Tests', () => {
  describe('StudentInfo Management Update Component', () => {
    let comp: StudentInfoUpdateComponent;
    let fixture: ComponentFixture<StudentInfoUpdateComponent>;
    let service: StudentInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [StudentInfoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StudentInfoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentInfoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudentInfoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudentInfo(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new StudentInfo();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
