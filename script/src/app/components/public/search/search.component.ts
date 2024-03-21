import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { JobCategoryService } from '../../../services/job-category/job-category.service';
import { JobService } from '../../../services/job/job.service';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss',
})
export class SearchComponent implements OnInit, OnDestroy {
  //form
  totalPage: number = 0;
  page: number = 0;
  searchForm!: FormGroup;
  advancedSearch: boolean = false;
  careerLevels: string[] = ['Manager', 'Fresher', 'Junior', 'Senior', 'Intern'];
  experiences: any[] = [
    { name: 'Fresh', value: 0 },
    { name: 'Less Than 1 Year', value: 1 },
    { name: '2 Years', value: 2 },
    { name: '3 Years', value: 3 },
    { name: '4 Years', value: 4 },
    { name: '5 Years', value: 5 },
    { name: '6 Years', value: 6 },
    { name: '7 Years', value: 7 },
    { name: '8 Years', value: 8 },
  ];
  jobType: string[] = ['FullTime', 'PartTime'];
  offerSalaries = [
    { label: '$0-$1000', value: '0-$1000' },
    { label: '$1000-$2000', value: '$1000-$2000' },
    { label: '$2000-$3000', value: '$2000-$3000' },
    { label: '$3000-$5000', value: '$3000-$5000' },
    { label: '$5000++', value: '$5000++' },
    { label: 'Negotiable', value: 'Negotiable' },
  ];
  qualifications = [
    { label: 'Certificate', value: 'Certificate' },
    { label: 'Diploma', value: 'Diploma' },
    { label: 'Associate Degree', value: 'Associate Degree' },
    { label: 'Bachelor Degree', value: 'Bachelor Degree' },
    { label: 'Master’s Degree', value: 'Master’s Degree' },
    { label: 'Doctorate Degree', value: 'Doctorate Degree' },
  ];
  listCategory: any;
  branchList: any;
  jobList: any;

  constructor(
    private fb: FormBuilder,
    private routes: ActivatedRoute,
    private jobCate: JobCategoryService,
    private jobService: JobService,
    private msg: NzMessageService,
    private route: Router
  ) {
    this.searchForm = fb.group({
      textSearch: [null],
      category: [null],
      location: [null],
      careerLevel: [null],
      experience: [null],
      offerSalary: [null],
      qualification: [null],
    });
  }
  ngOnInit(): void {
    this.routes.queryParams.subscribe((params) => {
      console.log('====================================');
      console.log('params::', params);
      console.log('====================================');

      if (!params) {
        const formData = new FormData();
        formData.append('text', this.searchForm.value.textSearch || '');
        formData.append('category', this.searchForm.value.category || '');
        formData.append('branch', this.searchForm.value.location || '');
        // Inside your ngOnInit method
        this.jobService.searchJob(1, formData).subscribe(
          (res) => {
            console.log('====================================');
            console.log('searching::', res);
            console.log('====================================');
            this.page = res.page;
            this.totalPage = res.totalPage * 10; // Assuming 10 items per page
            this.jobList = res.listResults;
          },
          (err) => {
            this.jobList = [];
            this.totalPage = 0;
            this.msg.error(err?.error || 'Not found');
          }
        );
      }

      if (params['title'] || params['title'] === '') {
        const formData = new FormData();
        formData.append('text', params['title'] || '');
        this.jobService.searchJob(1, formData).subscribe(
          (res) => {
            this.page = res.page;
            this.totalPage = res.totalPage * 10;
            this.jobList = res.listResults;
          },
          (err) => {
            this.jobList = [];
            this.totalPage = 0;
            this.msg.error(err?.error || 'Not found');
          }
        );
      }

      if (params['branchId']) {
        const formData = new FormData();
        formData.append('branch', params['branchId'] || '');
        this.jobService.searchJob(1, formData).subscribe(
          (res) => {
            this.jobList = res.listResults;
            this.page = res.page;
            this.totalPage = res.totalPage * 10;
          },
          (err) => {
            this.jobList = [];
            this.totalPage = 0;
            this.msg.error(err?.error || 'Not found');
          }
        );
      }

      if (params['categoryId']) {
        const formData = new FormData();
        formData.append('category', params['categoryId'] || '');
        this.jobService.searchJob(1, formData).subscribe(
          (res) => {
            this.jobList = res.listResults;
            this.page = res.page;
            this.totalPage = res.totalPage * 10;
          },
          (err) => {
            this.jobList = [];
            this.totalPage = 0;
            this.msg.error(err?.error || 'Not found');
          }
        );
      }
    });

    this.jobCate.viewJobCategory().subscribe(
      (res) => {
        this.listCategory = res;
        console.log('====================================');
        console.log('res::', res);
        console.log('====================================');
      },
      (err) => {
        console.log('====================================');
        console.log('err::', err);
        console.log('====================================');
      }
    );

    this.jobCate.getAllBranch().subscribe(
      (res) => {
        this.branchList = res;
        console.log('====================================');
        console.log('res::', res);
        console.log('====================================');
      },
      (err) => {
        console.log('====================================');
        console.log('err::', err);
        console.log('====================================');
      }
    );
  }

  advanced() {
    this.advancedSearch = !this.advancedSearch;
  }

  submitForm() {
    const formData = new FormData();
    formData.append('text', this.searchForm.value.textSearch || '');
    formData.append('category', this.searchForm.value.category || '');
    formData.append('branch', this.searchForm.value.location || '');
    if (this.searchForm.value.experience) {
      if (this.advancedSearch === true) {
        if (this.searchForm.value.careerLevel) {
          formData.append(
            'career_level',
            this.searchForm.value.careerLevel || ''
          );
        }

        if (this.searchForm.value.experience) {
          formData.append('experience', this.searchForm.value.experience);
        }

        if (this.searchForm.value.offerSalary) {
          formData.append('salary', this.searchForm.value.offerSalary || '');
        }

        if (this.searchForm.value.qualification) {
          formData.append(
            'qualification',
            this.searchForm.value.qualification || ''
          );
        }
      }
    } else {
      if (this.advancedSearch === true) {
        if (this.searchForm.value.careerLevel) {
          formData.append(
            'career_level',
            this.searchForm.value.careerLevel || ''
          );
        }

        if (this.searchForm.value.offerSalary) {
          formData.append('salary', this.searchForm.value.offerSalary || '');
        }

        if (this.searchForm.value.qualification) {
          formData.append(
            'qualification',
            this.searchForm.value.qualification || ''
          );
        }
      }
    }

    this.jobService.searchJob(1, formData).subscribe(
      (res) => {
        this.jobList = res.listResults;
        this.page = res.page;
        this.totalPage = res.totalPage * 10;
      },
      (err) => {
        this.jobList = [];
        this.totalPage = 0;
        this.msg.error(err?.error || 'Not found');
      }
    );
  }

  pageActive(pageActive: number): void {
    const formData = new FormData();
    formData.append('text', this.searchForm.value.textSearch || '');
    formData.append('category', this.searchForm.value.category || '');
    formData.append('branch', this.searchForm.value.location || '');
    if (this.advancedSearch === true) {
      if (this.searchForm.value.careerLevel) {
        formData.append(
          'career_level',
          this.searchForm.value.careerLevel || ''
        );
      }

      if (this.searchForm.value.offerSalary) {
        formData.append('salary', this.searchForm.value.offerSalary || '');
      }

      if (this.searchForm.value.qualification) {
        formData.append(
          'qualification',
          this.searchForm.value.qualification || ''
        );
      }
    }
    this.jobService.searchJob(pageActive, formData).subscribe(
      (res) => {
        console.log('====================================');
        console.log('searching::', res);
        console.log('====================================');
        this.page = res.page;
        this.totalPage = res.totalPage * 10; // Assuming 10 items per page
        this.jobList = res.listResults;
      },
      (err) => {
        this.jobList = [];
        this.totalPage = 0;
        this.msg.error(err?.error || 'Not found');
      }
    );
  }
  clickItem(data: any) {
    this.route.navigate(['/detail', data?.id]);
  }

  parseDateString(dateString: string): Date {
    // Split the date string into year, month, and day parts
    const [year, month, day] = dateString.split('-').map(Number);
    // Create a new Date object (month is 0-based, so subtract 1)
    return new Date(year, month - 1, day);
  }

  getCurrentDate(): Date {
    return new Date(); // Returns the current date
  }

  ngOnDestroy(): void {}
}
