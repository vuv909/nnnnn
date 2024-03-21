import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd/message';
import { JobCategoryService } from '../../../services/job-category/job-category.service';

interface Person {
  key: string;
  name: string;
  age: number;
  address: string;
}

@Component({
  selector: 'app-job-category',
  templateUrl: './job-category.component.html',
  styleUrl: './job-category.component.scss',
})
export class JobCategoryComponent implements OnInit {
  isAddSpinning: boolean = false;
  isUpdateSpinning: boolean = false;
  addFormData!: FormData;
  updateFormData!: FormData;
  isVisibleAdd = false;
  isVisibleUpdate = false;
  idUpdate!: number;
  addForm!: FormGroup;
  totalPage: number = 0;
  page: number = 1;

  updateForm!: FormGroup;

  listJobCategory: any = null;

  constructor(
    private msg: NzMessageService,
    private jobCategoryService: JobCategoryService
  ) {
    this.addForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(100),
      ]),
      image: new FormControl(null, Validators.required),
    });
    this.updateForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(100),
      ]),
      image: new FormControl(null),
    });
  }

  ngOnInit(): void {
    this.jobCategoryService.viewJobCategory(1).subscribe(
      (res) => {
        this.listJobCategory = res.listResults;
        this.totalPage = res.totalPage;
        this.page = res.page;
      },
      (err) => {
        this.msg.error('Error occurred !!!');
        this.listJobCategory = [];
        this.totalPage = 0;
        this.page = 0;
      }
    );
  }

  pageActive(pageActive: number): void {
    this.jobCategoryService.viewJobCategory(pageActive).subscribe(
      (res) => {
        this.listJobCategory = res.listResults;
        this.totalPage = res.totalPage;
        this.page = res.page;
      },
      (err) => {
        this.msg.error('Error occurred !!!');
        this.listJobCategory = [];
        this.totalPage = 0;
        this.page = 0;
      }
    );
  }

  showModal(): void {
    this.isVisibleAdd = true;
  }

  showModalUpdate(name: string, id: number): void {
    this.idUpdate = id;
    this.updateForm.patchValue({
      name: name,
    });
    this.isVisibleUpdate = true;
  }

  handleOk(): void {
    this.isAddSpinning = true;
    console.log('Button ok clicked!');
    console.log('====================================');
    console.log('form::', this.addForm);
    console.log('====================================');
    if (this.addForm.get('name')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error(
        'Vui lòng không để trống trường tên và nhập nhỏ hơn 100 kí tự !!!'
      );
    } else if (this.addForm.get('image')!.invalid) {
      this.isAddSpinning = false;
      this.msg.error('Vui lòng chọn ảnh hợp lệ và không để trống !!!');
    } else {
      this.addFormData = new FormData();
      const file: File = this.addForm.value.image;
      const fileSizeInMB = file.size / (1024 * 1024); // Convert bytes to MB
      if (fileSizeInMB > 2) {
        this.msg.error('Kích thước ảnh không được vượt quá 2MB !!!');
        this.isAddSpinning = false;
        return; // Prevent form submission if file size exceeds 2MB
      }
      console.log('====================================');
      console.log(this.addForm.value);
      console.log('====================================');
      this.addFormData.append('name', this.addForm.value.name);
      this.addFormData.append('image', this.addForm.value.image);
      this.jobCategoryService.addJobCategory(this.addFormData).subscribe(
        (res) => {
          this.addForm.reset();
          this.msg.success('Add successfully!!');
          this.jobCategoryService.viewJobCategory(this.page).subscribe(
            (res) => {
              this.listJobCategory = res.listResults;
              this.totalPage = res.totalPage;
              this.page = res.page;
              this.isAddSpinning = false;
              this.handleCancel();
            },
            (err) => {
              this.handleCancel();
              this.isAddSpinning = false;
              this.msg.error('Error occurred !!!');
              this.listJobCategory = [];
              this.totalPage = 0;
              this.page = 0;
            }
          );
        },
        (error) => {
          this.isAddSpinning = false;
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Add job failed', { nzDuration: 2000 });
          }
        }
      );
    }
  }

  handleCancel(): void {
    console.log('Button cancel clicked!');
    this.isVisibleAdd = false;
  }

  handleOkUpdate(): void {
    this.isUpdateSpinning = true;
    console.log('Button ok clicked!');
    console.log('====================================');
    console.log('form::', this.updateForm);
    console.log('====================================');
    if (this.updateForm.get('name')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error(
        'Vui lòng không để trống trường tên và nhập nhỏ hơn 100 kí tự !!!'
      );
    } else if (this.updateForm.get('image')!.invalid) {
      this.isUpdateSpinning = false;
      this.msg.error('Vui lòng chọn ảnh hợp lệ và không để trống !!!');
    } else {
      if (this.updateForm.value.image) {
        const file: File = this.updateForm.value.image;
        const fileSizeInMB = file.size / (1024 * 1024); // Convert bytes to MB
        if (fileSizeInMB > 2) {
          this.msg.error('Kích thước ảnh không được vượt quá 2MB !!!');
          this.isUpdateSpinning = false;
          return; // Prevent form submission if file size exceeds 2MB
        }
      }
      this.updateFormData = new FormData();
      console.log('====================================');
      console.log(this.updateForm.value);
      console.log('====================================');
      this.updateFormData.append('id', String(this.idUpdate));
      this.updateFormData.append('name', this.updateForm.value.name);
      if (this.updateForm.value.image !== null) {
        this.updateFormData.append('image', this.updateForm.value.image);
      }
      console.log('====================================');
      console.log('id::', this.idUpdate);
      console.log('====================================');
      this.jobCategoryService.updateJobCategory(this.updateFormData).subscribe(
        (res) => {
          this.updateForm.reset();
          this.msg.success('Update successfully!!');
          this.jobCategoryService.viewJobCategory(this.page).subscribe(
            (res) => {
              this.listJobCategory = res.listResults;
              this.totalPage = res.totalPage;
              this.page = res.page;
              this.isUpdateSpinning = false;
              this.handleCancelUpdate();
            },
            (err) => {
              this.handleCancelUpdate();
              this.isUpdateSpinning = false;
              this.msg.error('Error occurred !!!');
              this.listJobCategory = [];
              this.totalPage = 0;
              this.page = 0;
            }
          );
        },
        (error) => {
          this.isUpdateSpinning = false;
          this.msg.error('Error occured !!!');
        }
      );
    }
  }

  handleCancelUpdate(): void {
    console.log('Button cancel clicked!');
    this.isVisibleUpdate = false;
  }

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files && inputElement.files.length > 0) {
      const file: File = inputElement.files[0];
      this.addForm.patchValue({
        image: file,
      });
    }
  }

  onFileSelectedUpdate(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files && inputElement.files.length > 0) {
      const file: File = inputElement.files[0];
      this.updateForm.patchValue({
        image: file,
      });
    }
  }

  deleteJobCategory(id: number) {
    this.jobCategoryService.deleteJobCategoryById(id).subscribe(
      (res) => {
        this.msg.success('Delete successfully !!!');

        this.jobCategoryService.viewJobCategory(this.page).subscribe(
          (res) => {
            this.listJobCategory = res.listResults;
            this.totalPage = res.totalPage;
            this.page = res.page;
          },
          (err) => {
            this.msg.error('Error occurred !!!');
            this.listJobCategory = [];
            this.totalPage = 0;
            this.page = 0;
          }
        );
      },
      (res) => {
        this.msg.error('Error occured while deleting !!!');
      }
    );
  }
}
