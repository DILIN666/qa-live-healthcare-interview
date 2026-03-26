<template>
  <div class="doctors-page">
    <div class="page-header">
      <h1>医生团队</h1>
      <p>我们的专业医疗团队随时为您服务</p>
    </div>

    <div class="doctors-container">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <a-spin tip="加载中...">
          <div class="loading-content"></div>
        </a-spin>
      </div>

      <!-- 错误状态 -->
      <div v-else-if="error" class="error-container">
        <a-alert
          type="error"
          :message="error"
          show-icon
          :closable="false"
        />
        <a-button type="primary" @click="fetchDoctors" style="margin-top: 16px;">
          重试
        </a-button>
      </div>

      <!-- 医生列表 -->
      <div v-else>
        <div class="doctors-grid">
          <a-card
            v-for="doctor in doctors"
            :key="doctor.id"
            class="doctor-card"
            :class="{ 'active': doctor.isActive }"
          >
            <div class="card-header">
              <img :src="doctor.avatar" :alt="doctor.name" class="doctor-avatar" />
              <a-badge
                :status="doctor.isActive ? 'processing' : 'default'"
                :text="doctor.isActive ? '在线' : '离线'"
              />
            </div>
            <div class="card-body">
              <h3>{{ doctor.name }}</h3>
              <p class="doctor-title">{{ doctor.title }}</p>
              <p class="doctor-department">{{ doctor.department }}</p>
              <p class="doctor-experience">{{ doctor.experience }}</p>
              <div class="doctor-specialties">
                <a-tag v-for="specialty in doctor.specialties" :key="specialty" color="blue">
                  {{ specialty }}
                </a-tag>
              </div>
            </div>
            <div class="card-footer">
              <a-button
                type="primary"
                block
                :disabled="!doctor.isActive"
                @click="goToConsultation(doctor)"
              >
                {{ doctor.isActive ? '进入诊室' : '暂未开放' }}
              </a-button>
            </div>
          </a-card>
        </div>

        <!-- 分页 -->
        <div class="pagination-container">
          <a-pagination
            v-model:current="currentPage"
            :page-size="pageSize"
            :total="total"
            :show-total="(total) => `共 ${total} 位医生`"
            @change="handlePageChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// 状态管理
const doctors = ref<any[]>([]);
const loading = ref(false);
const error = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 接口URL
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/doctors';

// 获取医生列表
const fetchDoctors = async () => {
  loading.value = true;
  error.value = '';
  
  try {
    const response = await fetch(`${API_URL}?page=${currentPage.value - 1}&size=${pageSize.value}`);
    if (!response.ok) {
      throw new Error('网络请求失败');
    }
    const data = await response.json();
    if (data.success) {
      doctors.value = data.data;
      total.value = data.total;
    } else {
      throw new Error(data.message || '获取数据失败');
    }
  } catch (err) {
    error.value = err instanceof Error ? err.message : '未知错误';
    doctors.value = [];
    total.value = 0;
  } finally {
    loading.value = false;
  }
};

// 分页变化处理
const handlePageChange = (page: number) => {
  currentPage.value = page;
  fetchDoctors();
};

// 进入诊室
const goToConsultation = (doctor: any) => {
  router.push(`/consultation/${doctor.username}`);
};

// 组件挂载时获取数据
onMounted(() => {
  fetchDoctors();
});
</script>

<style scoped>
.doctors-page {
  min-height: calc(100vh - 64px);
  padding-top: 64px;
  background: #f0f2f5;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 80px 24px;
  text-align: center;
  color: #fff;
}

.page-header h1 {
  font-size: 48px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 16px;
}

.page-header p {
  font-size: 20px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
}

.doctors-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 48px 24px;
}

.doctors-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.doctor-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.3s;
}

.doctor-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-4px);
}

.doctor-card.active {
  border: 2px solid #52c41a;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.doctor-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.card-body h3 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.doctor-title {
  font-size: 16px;
  color: #1890ff;
  font-weight: 500;
  margin-bottom: 4px;
}

.doctor-department {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.doctor-experience {
  font-size: 14px;
  color: #999;
  margin-bottom: 16px;
}

.doctor-specialties {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.loading-content {
  width: 200px;
  height: 200px;
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  padding: 24px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 32px;
  }

  .page-header p {
    font-size: 16px;
  }

  .doctors-grid {
    grid-template-columns: 1fr;
  }

  .pagination-container {
    padding: 0 24px;
  }
}
</style>
