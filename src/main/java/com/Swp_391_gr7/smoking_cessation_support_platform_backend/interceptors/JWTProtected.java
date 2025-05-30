package com.Swp_391_gr7.smoking_cessation_support_platform_backend.interceptors;
import java.lang.annotation.*;



@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JWTProtected {
}
