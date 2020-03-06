import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { StudentInfoDetailComponent } from 'app/entities/student-info/student-info-detail.component';
import { StudentInfo } from 'app/shared/model/student-info.model';

describe('Component Tests', () => {
  describe('StudentInfo Management Detail Component', () => {
    let comp: StudentInfoDetailComponent;
    let fixture: ComponentFixture<StudentInfoDetailComponent>;
    const route = ({ data: of({ studentInfo: new StudentInfo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [StudentInfoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StudentInfoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentInfoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentInfo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentInfo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
