require "json"
fabric_enabled = ENV['RCT_NEW_ARCH_ENABLED'] == '1'

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNKeyboardInsets"
  s.version      = package["version"]
  s.summary      = package["description"]
 
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]
  s.platforms    = { :ios => "10.0" }
  s.source       = { :git => "https://github.com/github-account/keyboard-insets.git", :tag => "#{s.version}" }

  if fabric_enabled
    folly_compiler_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32'

    s.pod_target_xcconfig = {
      'HEADER_SEARCH_PATHS' => '"$(PODS_ROOT)/boost" "$(PODS_ROOT)/boost-for-react-native"  "$(PODS_ROOT)/RCT-Folly"',
      "CLANG_CXX_LANGUAGE_STANDARD" => "c++17",
    }
    s.compiler_flags  = folly_compiler_flags + ' -DRCT_NEW_ARCH_ENABLED'
    s.source_files    = "ios/KeyboardInsets/**/*.{h,m,mm,cpp}"

    install_modules_dependencies(s)
  else
    s.source_files = "ios/KeyboardInsets/**/*.{h,m,mm}"
    s.dependency "React-Core"
  end
end
