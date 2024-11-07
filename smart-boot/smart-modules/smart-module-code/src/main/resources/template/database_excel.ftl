<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:html="http://www.w3.org/TR/REC-html40">
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Author>GCCodeGenerator</Author>
  <LastAuthor>GCCodeGenerator</LastAuthor>
  <Created>2021-05-19T08:54:15Z</Created>
  <LastSaved>2021-05-19T08:54:15Z</LastSaved>
  <Version>16.00</Version>
 </DocumentProperties>
 <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
  <AllowPNG/>
 </OfficeDocumentSettings>
 <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
  <WindowHeight>8928</WindowHeight>
  <WindowWidth>23040</WindowWidth>
  <WindowTopX>32767</WindowTopX>
  <WindowTopY>32767</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal">
   <Alignment ss:Vertical="Center"/>
   <Borders/>
   <Font ss:FontName="等线" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss:ID="m2553513267088">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="2"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#008000"/>
   <Interior ss:Color="#C6EFCE" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s69">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#008000"/>
   <Interior ss:Color="#C6EFCE" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s70">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#008000"/>
   <Interior ss:Color="#C6EFCE" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s71">
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
  </Style>
  <Style ss:ID="s72">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#000000"/>
  </Style>
  <Style ss:ID="s73">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="11" ss:Color="#000000"/>
  </Style>
 </Styles>
 <Worksheet ss:Name="数据库设计文档">
  <Table ss:ExpandedColumnCount="7" ss:ExpandedRowCount="${tableList?size * 2 + columnSize + 20}" x:FullColumns="1"
   x:FullRows="1" ss:DefaultRowHeight="13.8">
   <Column ss:Index="2" ss:AutoFitWidth="0" ss:Width="142.80000000000001"/>
   <Column ss:AutoFitWidth="0" ss:Width="141"/>
   <Column ss:AutoFitWidth="0" ss:Width="61.8"/>
   <Column ss:AutoFitWidth="0" ss:Width="119.4"/>
   <Column ss:Index="7" ss:AutoFitWidth="0" ss:Width="90"/>
   <#list tableList as table>
  <Row ss:AutoFitHeight="0" ss:Height="25.049999999999997">
    <Cell ss:MergeAcross="6" ss:StyleID="m2553513267088"><Data ss:Type="String">${table.tableName} ${table.remarks ! ''}</Data></Cell>
   </Row>
  <Row ss:AutoFitHeight="0" ss:Height="14.55">
    <Cell ss:StyleID="s69"><Data ss:Type="String">序号</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">字段</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">描述</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">主键</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">类型</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">是否为空</Data></Cell>
    <Cell ss:StyleID="s70"><Data ss:Type="String">默认值</Data></Cell>
   </Row>
   <#if table.primaryKeyList??>
    <#list table.primaryKeyList as primaryKey>
   <Row ss:AutoFitHeight="0" ss:Height="16.95">
    <Cell ss:StyleID="s71"><Data ss:Type="Number">${primaryKey_index + 1}</Data></Cell>
    <Cell ss:StyleID="s72"><Data ss:Type="String">${primaryKey.columnName}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${primaryKey.remarks ! ''}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">Y</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${primaryKey.typeName}(${(primaryKey.typeName == 'NUMBER' && primaryKey.columnSize == 0 && primaryKey.decimalDigits == -127) ? string( '*' , primaryKey.columnSize)})</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">N</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${primaryKey.columnDef ! ''}</Data></Cell>
   </Row>
    </#list>
   </#if>

   <#if table.baseColumnList??>
    <#list table.baseColumnList as column>
      <Row ss:AutoFitHeight="0" ss:Height="16.95">
    <Cell ss:StyleID="s71"><Data ss:Type="Number"><#if table.primaryKeyList??>${table.primaryKeyList?size + column_index + 1}<#else >${column_index + 1}</#if></Data></Cell>
    <Cell ss:StyleID="s72"><Data ss:Type="String">${column.columnName}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${column.remarks ! ''}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String"></Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${column.typeName}(${(column.typeName == 'NUMBER' && column.columnSize == 0 && column.decimalDigits == -127) ? string( '*' , column.columnSize)})</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${(column.nullable == 0) ? string('N', '')}</Data></Cell>
    <Cell ss:StyleID="s73"><Data ss:Type="String">${column.columnDef ! ''}</Data></Cell>
   </Row>
    </#list>
   </#if>
   </#list>

  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <PageSetup>
    <Header x:Margin="0.3"/>
    <Footer x:Margin="0.3"/>
    <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
   </PageSetup>
   <Unsynced/>
   <Print>
    <ValidPrinterInfo/>
    <PaperSizeIndex>9</PaperSizeIndex>
    <HorizontalResolution>600</HorizontalResolution>
    <VerticalResolution>600</VerticalResolution>
   </Print>
   <Selected/>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>2</ActiveRow>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
</Workbook>
