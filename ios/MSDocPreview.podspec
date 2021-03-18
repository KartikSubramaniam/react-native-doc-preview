
Pod::Spec.new do |s|
  s.name         = "MSDocPreview"
  s.version      = "1.0.0"
  s.summary      = "MSDocPreview"
  s.description  = <<-DESC
                  MSDocPreview
                   DESC
  s.homepage     = "HOMEPAGE"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/KartikSubramaniam/react-native-doc-preview.git", :tag => "master" }
  s.source_files  = '**/*.{h,m}'
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end