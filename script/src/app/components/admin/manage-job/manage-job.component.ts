import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd/message';
import { JobCategoryService } from '../../../services/job-category/job-category.service';
import { JobService } from '../../../services/job/job.service';
import { formatDate } from '@angular/common';
import { StorageService } from '../../../services/storage/storage.service';

@Component({
  selector: 'app-manage-job',
  templateUrl: './manage-job.component.html',
  styleUrl: './manage-job.component.scss',
})
export class ManageJobComponent implements OnInit {
  isVisibleAdd = false;
  isVisibleUpdate = false;
  listJob: any = null;
  idUpdate!: number;
  isAddSpinning: boolean = false;
  isUpdateSpinning: boolean = false;
  addForm: FormGroup;
  jobInfo: any;
  updateForm!: FormGroup;
  listJobCategory: any = null;
  branchList: any = null;
  totalPage: number = 0;
  page: number = 1;
  careerLevels: string[] = ['Manager', 'Fresher', 'Junior', 'Senior', 'Intern'];
  experiences: string[] = [
    'Fresh',
    'Less Than 1 Year',
    '2 Years',
    '3 Years',
    '4 Years',
    '5 Years',
    '6 Years',
    '7 Years',
    '8 Years',
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

  applyBeforeValidator(
    control: AbstractControl
  ): { [key: string]: any } | null {
    const currentDate = new Date();
    const applyBeforeDate = new Date(control.value);
    const sevenDaysLater = new Date(
      currentDate.setDate(currentDate.getDate() + 7)
    ); // Calculate 7 days later

    if (applyBeforeDate <= sevenDaysLater) {
      return { applyBeforeInvalid: true };
    }
    return null;
  }

  constructor(
    private msg: NzMessageService,
    private jobCategoryService: JobCategoryService,
    private jobService: JobService
  ) {
    this.addForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(100),
      ]),
      Job_Type: new FormControl(null, Validators.required),
      category: new FormControl(null, Validators.required),
      branch: new FormControl(null, Validators.required),
      Career_Level: new FormControl(null, Validators.required),
      experience: new FormControl(null, Validators.required),
      offerSalary: new FormControl(null, Validators.required),
      qualification: new FormControl(null, Validators.required),
      Apply_Before: new FormControl(null, [
        Validators.required,
        this.applyBeforeValidator,
      ]),
      address: new FormControl(null, Validators.required),
      description: new FormControl(null, [
        Validators.required,
        Validators.maxLength(750),
      ]),
    });
  }

  initialUpdateForm(): void {
    const defaultDateValue = this.jobInfo.apply_Before
      ? new Date(this.jobInfo.apply_Before)
      : null;

    const formattedDate = defaultDateValue
      ? formatDate(defaultDateValue, 'yyyy-MM-dd', 'en-US')
      : null;

    this.updateForm = new FormGroup({
      name: new FormControl(this.jobInfo.name || '', [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(100),
      ]),
      Job_Type: new FormControl(
        this.jobInfo.job_Type || null,
        Validators.required
      ),
      category: new FormControl(
        this.jobInfo.category_Id || null,
        Validators.required
      ),
      branch: new FormControl(
        this.jobInfo.branch_Id || null,
        Validators.required
      ), // Initialize with the branch ID from jobInfo
      Career_Level: new FormControl(
        this.jobInfo.career_Level || null,
        Validators.required
      ),
      experience: new FormControl(
        this.jobInfo.experience || null,
        Validators.required
      ),
      offerSalary: new FormControl(
        this.jobInfo.offer_Salary || null,
        Validators.required
      ),
      qualification: new FormControl(
        this.jobInfo.qualification || null,
        Validators.required
      ),
      Apply_Before: new FormControl(formattedDate, [Validators.required]),

      address: new FormControl(
        this.jobInfo.address || null,
        Validators.required
      ),
      description: new FormControl(this.jobInfo.description || null, [
        Validators.required,
        Validators.maxLength(750),
      ]),
    });
  }

  ngOnInit(): void {
    this.jobService.getJobByHrEmail(StorageService.getEmail(), 1).subscribe(
      (res) => {
        this.listJob = res.listResults;
        this.totalPage = res.totalPage;
        this.page = res.page;
      },
      (error) => {
        this.listJob = [];
        this.totalPage = 0;
        this.page = 0;
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Error occur', { nzDuration: 2000 });
        }
      }
    );
    this.jobCategoryService.viewJobCategory().subscribe(
      (res) => {
        this.listJobCategory = res;
      },
      (error) => {
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Error occur', { nzDuration: 2000 });
        }
      }
    );
    this.jobCategoryService.getAllBranch().subscribe(
      (res) => {
        this.branchList = res;
      },
      (error) => {
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Error occur', { nzDuration: 2000 });
        }
      }
    );
  }

  pageActive(pageActive: number): void {
    this.jobService
      .getJobByHrEmail(StorageService.getEmail(), pageActive)
      .subscribe(
        (res) => {
          this.listJob = res.listResults;
          this.totalPage = res.totalPage;
          this.page = res.page;
        },
        (error) => {
          this.listJob = [];
          this.totalPage = 0;
          this.page = 0;
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Error occur', { nzDuration: 2000 });
          }
        }
      );
  }

  showModal(): void {
    this.isVisibleAdd = true;
  }

  showModalUpdate(id: number): void {
    this.idUpdate = id;

    this.jobService.getJobById(id).subscribe(
      (res) => {
        this.jobInfo = res;
        this.initialUpdateForm();
        this.isVisibleUpdate = true;
      },
      (err) => {
        this.msg.error('Error occurred !!!');
        this.branchList = null;
      }
    );
  }

  handleCancel(): void {
    console.log('Button cancel clicked!');
    this.isVisibleAdd = false;
  }

  handleOk(): void {
    this.isAddSpinning = true;

    if (
      this.addForm.get('name')!.invalid ||
      this.addForm.get('name')!.value.trim() === ''
    ) {
      this.isAddSpinning = false;
      this.msg.error(
        'Vui lòng không để trống trường tên và nhập nhỏ hơn 100 kí tự !!!'
      );
    } else if (this.addForm.get('Job_Type')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng không để trống trường loại công việc !!!');
    } else if (this.addForm.get('category')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng không để trống trường phân loại !!!');
    } else if (this.addForm.get('branch')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng không để trống trường chi nhánh !!!');
    } else if (this.addForm.get('Career_Level')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng không để trống trường trình độ !!!');
    } else if (this.addForm.get('experience')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng không để trống trường kinh nghiệm !!!');
    } else if (this.addForm.get('offerSalary')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng không để trống trường lương !!!');
    } else if (this.addForm.get('qualification')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng không để trống bằng cấp !!!');
    } else if (this.addForm.get('Apply_Before')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error(
        'Vui lòng không để trống trường ứng tuyển trước và hạn ứng tuyển ít nhất là 7 ngày !!!'
      );
    } else if (
      this.addForm.get('address')!.invalid ||
      this.addForm.get('address')!.value.trim() === ''
    ) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng không để trống trường địa chỉ !!!');
    } else if (
      this.addForm.get('description')!.invalid ||
      this.addForm.get('description')!.value.trim() === ''
    ) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng không để trống trường mô tả !!!');
    } else {
      const formData = new FormData();
      formData.append('name', this.addForm.value.name);
      formData.append('Category_Id', this.addForm.value.category);
      formData.append('Career_Level', this.addForm.value.Career_Level);
      formData.append('Experience', this.addForm.value.experience);
      formData.append('Offer_Salary', this.addForm.value.offerSalary);
      formData.append('Qualification', this.addForm.value.qualification);
      formData.append('Job_Type', this.addForm.value.Job_Type);
      formData.append('Branch_Id', this.addForm.value.branch);
      formData.append('Description', this.addForm.value.description);
      formData.append('Apply_Before', this.addForm.value.Apply_Before);
      formData.append('Address', this.addForm.value.address);
      formData.append('hrEmail', StorageService.getEmail());
      this.jobService.addJob(formData).subscribe(
        (res) => {
          this.addForm.reset();
          this.msg.success('Job created successfully !!!');
          this.jobService
            .getJobByHrEmail(StorageService.getEmail(), this.page)
            .subscribe(
              (res) => {
                this.listJob = res.listResults;
                this.totalPage = res.totalPage;
                this.page = res.page;
                this.isAddSpinning = false;
                this.handleCancel();
              },
              (error) => {
                this.handleCancel();
                this.isAddSpinning = false;
                this.listJob = [];
                this.totalPage = 0;
                this.page = 0;
                if (error?.error?.error) {
                  this.msg.error(error?.error?.error, { nzDuration: 2000 });
                } else {
                  this.msg.error('Error occur', { nzDuration: 2000 });
                }
              }
            );
        },
        (error) => {
          this.isAddSpinning = false;
          this.handleCancel();
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Add job failed', {
              nzDuration: 2000,
            });
          }
        }
      );
    }
  }

  handleOkUpdate(): void {
    this.isUpdateSpinning = true;

    if (
      this.updateForm.get('name')!.invalid ||
      this.updateForm.get('name')!.value.trim() === ''
    ) {
      this.isUpdateSpinning = false;
      this.msg.error(
        'Vui lòng không để trống trường tên và nhập nhỏ hơn 100 kí tự !!!'
      );
    } else if (this.updateForm.get('Job_Type')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống trường loại công việc !!!');
    } else if (this.updateForm.get('category')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống trường phân loại !!!');
    } else if (this.updateForm.get('branch')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống trường chi nhánh !!!');
    } else if (this.updateForm.get('Career_Level')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống trường trình độ !!!');
    } else if (this.updateForm.get('experience')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống trường kinh nghiệm !!!');
    } else if (this.updateForm.get('offerSalary')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống trường lương !!!');
    } else if (this.updateForm.get('qualification')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống bằng cấp !!!');
    } else if (this.updateForm.get('Apply_Before')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống trường ứng tuyển !!!');
    } else if (
      this.updateForm.get('address')!.invalid ||
      this.updateForm.get('address')!.value.trim() === ''
    ) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống trường địa chỉ !!!');
    } else if (
      this.updateForm.get('description')!.invalid ||
      this.updateForm.get('description')!.value.trim() === ''
    ) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng không để trống trường mô tả !!!');
    } else {
      const formData = new FormData();
      formData.append('id', this.idUpdate.toString());
      formData.append('name', this.updateForm.value.name);
      formData.append('Category_Id', this.updateForm.value.category);
      formData.append('Career_Level', this.updateForm.value.Career_Level);
      formData.append('Experience', this.updateForm.value.experience);
      formData.append('Offer_Salary', this.updateForm.value.offerSalary);
      formData.append('Qualification', this.updateForm.value.qualification);
      formData.append('Job_Type', this.updateForm.value.Job_Type);
      formData.append('Branch_Id', this.updateForm.value.branch);
      formData.append('Description', this.updateForm.value.description);
      formData.append('Apply_Before', this.updateForm.value.Apply_Before);
      formData.append('Address', this.updateForm.value.address);

      this.jobService.updateJob(formData).subscribe(
        (res) => {
          this.jobService
            .getJobByHrEmail(StorageService.getEmail(), this.page)
            .subscribe(
              (res) => {
                this.listJob = res.listResults;
                this.totalPage = res.totalPage;
                this.page = res.page;
                this.isUpdateSpinning = false;
                this.handleCancelUpdate();
              },
              (error) => {
                this.isUpdateSpinning = false;
                this.handleCancelUpdate();
                this.listJob = [];
                this.totalPage = 0;
                this.page = 0;
                if (error?.error?.error) {
                  this.msg.error(error?.error?.error, { nzDuration: 2000 });
                } else {
                  this.msg.error('Error occur', { nzDuration: 2000 });
                }
              }
            );
        },
        (error) => {
          this.handleCancelUpdate();
          this.isUpdateSpinning = false;
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Error occur', { nzDuration: 2000 });
          }
        }
      );
    }
  }

  handleCancelUpdate(): void {
    console.log('Button cancel clicked!');
    this.isVisibleUpdate = false;
  }

  deleteJob(id: number) {
    this.jobService.deleteJobById(id).subscribe(
      (res) => {
        this.msg.success('Delete job successfully !!!');
        this.jobService
          .getJobByHrEmail(StorageService.getEmail(), this.page)
          .subscribe(
            (res) => {
              this.listJob = res.listResults;
              this.totalPage = res.totalPage;
              this.page = res.page;
            },
            (error) => {
              this.listJob = [];
              this.totalPage = 0;
              this.page = 0;
              if (error?.error?.error) {
                this.msg.error(error?.error?.error, { nzDuration: 2000 });
              } else {
                this.msg.error('Error occur', { nzDuration: 2000 });
              }
            }
          );
      },
      (error) => {
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Update job failed', { nzDuration: 2000 });
        }
      }
    );
  }
}
